package be.asers.service.impl;

import android.content.ContentValues;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import be.asers.bean.CountryBean;
import be.asers.bean.EpisodeBean;
import be.asers.bean.ExternalsBean;
import be.asers.bean.ImageBean;
import be.asers.bean.NetworkBean;
import be.asers.bean.SeasonBean;
import be.asers.bean.ShowBean;
import be.asers.dao.EpisodeDao;
import be.asers.dao.SeasonDao;
import be.asers.dao.ShowDao;
import be.asers.model.Episode;
import be.asers.model.Season;
import be.asers.model.Series;
import be.asers.model.Show;
import be.asers.service.FinderRemoteService;
import be.asers.service.FinderService;

/**
 * {@link Show} Finder service.
 * 
 * @author chesteric31
 */
public class FinderServiceImpl implements FinderService {

    /** The context. */
    private Context context;
    
    /** The show dao. */
    private ShowDao showDao;
    
    /** The season dao. */
    private SeasonDao seasonDao;
    
    /** The episode dao. */
    private EpisodeDao episodeDao;
    
    /** The remote service. */
    private FinderRemoteService remoteService;

    /**
     * Constructor.
     * 
     * @param context the {@link Context} to set
     */
    public FinderServiceImpl(Context context) {
        this.context = context;
        this.showDao = new ShowDao(this.context);
        this.seasonDao = new SeasonDao(this.context);
        this.episodeDao = new EpisodeDao(this.context);
        this.remoteService = new FinderRemoteServiceImpl(this.context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ShowBean> findMyShows() {
        List<Show> shows = showDao.findActiveShows();
        List<ShowBean> beans = new ArrayList<ShowBean>();
        if (!shows.isEmpty()) {
            for (Show show : shows) {
                ShowBean bean = mapShow(show);
                beans.add(bean);
            }
        }
        return beans;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShowBean findShow(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The series title cannot be empty!");
        }
        Show show = showDao.findByName(name);
        if (show == null) {
            return remoteService.findShow(name);
        } else {
            return mapShow(show);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShowBean addShow(ShowBean show) {
        ShowBean updatedShow = show;
        ContentValues values = mapShowContentValues(show);
        if (show.getId() != null) {
            showDao.update(values, show.getId());
        } else {
            Show addedShow = showDao.add(values);
            List<SeasonBean> seasons = show.getSeasons();
            if (seasons != null) {
                for (SeasonBean season : seasons) {
                    ContentValues seasonValues = mapSeasonContentValues(addedShow, season);
                    Season addedSeason = seasonDao.add(seasonValues);
                    addedSeason.setShow(addedShow);
                    List<EpisodeBean> episodes = season.getEpisodes();
                    for (EpisodeBean episode : episodes) {
                        ContentValues episodeValues = mapEpisodeContentValues(addedSeason, episode);
                        Episode addedEpisode = episodeDao.add(episodeValues);
                        addedEpisode.setSeason(addedSeason);
                        addedSeason.addEpisode(addedEpisode);
                    }
                    addedShow.addSeason(addedSeason);
                }
                updatedShow = mapShow(addedShow);
            }
        }
        return updatedShow;
    }

    /**
     * Maps a {@link EpisodeBean} to a {@link ContentValues}.
     * 
     * @param addedSeason the {@link Season} to use
     * @param episode the {@link EpisodeBean} to map
     * @return the mapped {@link ContentValues}
     */
    private ContentValues mapEpisodeContentValues(Season addedSeason, EpisodeBean episode) {
        ContentValues episodeValues = new ContentValues();
        Date airDate = episode.getAirDate();
        if (airDate != null) {
            episodeValues.put(Episode.COLUMN_AIR_DATE,
                    new SimpleDateFormat(Episode.DATE_PATTERN, Locale.US).format(airDate));
        } else {
            episodeValues.put(Episode.COLUMN_AIR_DATE, "");
        }
        episodeValues.put(Episode.COLUMN_EPISODE, episode.getEpisode());
        episodeValues.put(Episode.COLUMN_NUMBER, episode.getNumber());
        episodeValues.put(Episode.COLUMN_PRODUCTION_CODE, episode.getProductionCode());
        episodeValues.put(Episode.COLUMN_SEASON, addedSeason.getId());
        episodeValues.put(Episode.COLUMN_SPECIAL, episode.getSpecial());
        episodeValues.put(Episode.COLUMN_TITLE, episode.getTitle());
        episodeValues.put(Episode.COLUMN_TO_SEE, true);
        episodeValues.put(Episode.COLUMN_TV_RAGE_LINK, episode.getTvRageLink());
        return episodeValues;
    }

    /**
     * Maps a {@link SeasonBean} to a {@link ContentValues}.
     * 
     * @param show the {@link Show} to use
     * @param season the {@link SeasonBean} to map
     * @return the mapped {@link ContentValues}
     */
    private ContentValues mapSeasonContentValues(Show show, SeasonBean season) {
        ContentValues seasonValues = new ContentValues();
        seasonValues.put(Season.COLUMN_NUMBER, season.getNumber());
        seasonValues.put(Season.COLUMN_SERIES, show.getId());
        return seasonValues;
    }

    /**
     * Maps a {@link ShowBean} to a {@link ContentValues}.
     * 
     * @param show the {@link ShowBean} to map
     * @return the mapped {@link ContentValues}
     */
    private ContentValues mapShowContentValues(ShowBean show) {
        ContentValues values = new ContentValues();
        values.put(Show.COLUMN_ID, show.getId());
        NetworkBean network = show.getNetwork();
        values.put(Show.COLUMN_COUNTRY, network.getCountry().getCode());
        values.put(Show.COLUMN_NETWORK, network.getName());
        values.put(Show.COLUMN_RUN_TIME, show.getRunTime());
        values.put(Show.COLUMN_NAME, show.getName());
        values.put(Show.COLUMN_TV_RAGE_ID, show.getExternals().getTvRage());
        values.put(Show.COLUMN_TV_MAZE_ID, show.getTvMazeId());
        values.put(Show.COLUMN_STATUS, show.getStatus());
        ImageBean image = show.getImage();
        if (image != null) {
            values.put(Show.COLUMN_IMAGE, image.getMedium());
        }
        return values;
    }

    /**
     * Translates a {@link Show} model to a {@link ShowBean}.
     * 
     * @param show the {@link Show} to map
     * @return the mapped {@link ShowBean}
     */
    private ShowBean mapShow(Show show) {
        if (show != null) {
            ShowBean bean = new ShowBean();
            bean.setId(show.getId());
            NetworkBean networkBean = new NetworkBean();
            networkBean.setName(show.getNetwork());
            CountryBean countryBean = new CountryBean();
            countryBean.setCode(show.getCountry());
            networkBean.setCountry(countryBean);
            if (show.getCast() != null) {
                ImageBean imageBean = new ImageBean();
                imageBean.setMedium(show.getCast());
                bean.setImage(imageBean);
            }
            bean.setNetwork(networkBean);
            bean.setRunTime(show.getRunTime());
            bean.setName(show.getName());
            ExternalsBean externalsBean = new ExternalsBean();
            externalsBean.setTvRage(show.getTvRageId());
            bean.setExternals(externalsBean);
            bean.setTvMazeId(show.getTvMazeId());
            bean.setStatus(show.getStatus());
            List<SeasonBean> seasonBeans = mapSeasons(show, bean);
            bean.setSeasons(seasonBeans);
            return bean;
        } else {
            return null;
        }
    }

    /**
     * Maps {@link SeasonBean}s.
     * 
     * @param show the {@link Show} to use
     * @param bean the {@link ShowBean} to use
     * @return the mapped {@link SeasonBean}s
     */
    private List<SeasonBean> mapSeasons(Show show, ShowBean bean) {
        List<Season> seasons = seasonDao.findByShowId(show.getId());
        List<SeasonBean> seasonBeans = new ArrayList<SeasonBean>();
        if (seasons != null && !seasons.isEmpty()) {
            for (Season season : seasons) {
                SeasonBean seasonBean = new SeasonBean();
                seasonBean.setId(season.getId());
                seasonBean.setNumber(season.getNumber());
                List<Episode> episodes = episodeDao.findAllForSeason(season);
                List<EpisodeBean> episodesBeans = new ArrayList<EpisodeBean>();
                if (episodes != null && !episodes.isEmpty()) {
                    for (Episode episode : episodes) {
                        EpisodeBean episodeBean = mapEpisode(seasonBean, episode);
                        episodesBeans.add(episodeBean);
                    }
                }
                seasonBean.setEpisodes(episodesBeans);
                seasonBean.setShow(bean);
                seasonBeans.add(seasonBean);
            }
        }
        return seasonBeans;
    }

    /**
     * Maps a {@link EpisodeBean} from {@link SeasonBean} and {@link Episode}.
     * 
     * @param seasonBean the {@link SeasonBean} to use
     * @param episode the {@link Episode} to map
     * @return the mapped {@link EpisodeBean}
     */
    private EpisodeBean mapEpisode(SeasonBean seasonBean, Episode episode) {
        EpisodeBean episodeBean = new EpisodeBean();
        episodeBean.setAirDate(episode.getAirDate());
        episodeBean.setEpisode(episode.getEpisode());
        episodeBean.setId(episode.getId());
        episodeBean.setNumber(episode.getNumber());
        episodeBean.setProductionCode(episode.getProductionCode());
        episodeBean.setSeason(seasonBean);
        episodeBean.setSpecial(episode.getSpecial());
        episodeBean.setTitle(episode.getTitle());
        episodeBean.setTvRageLink(episode.getTvRageLink());
        return episodeBean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMyShow(ShowBean show) {
        show = findShow(show.getName());
        show.setStatus(Series.STATUS_ACTIVE);
        addShow(show);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMyShow(ShowBean show) {
        show.setStatus(Series.STATUS_INACTIVE);
        ContentValues contentValues = mapShowContentValues(show);
        showDao.update(contentValues, show.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShowDao getShowDao() {
        return showDao;
    }

}
