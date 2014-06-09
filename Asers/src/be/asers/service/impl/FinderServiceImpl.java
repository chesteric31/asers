package be.asers.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import be.asers.bean.EpisodeBean;
import be.asers.bean.SeasonBean;
import be.asers.bean.SeriesBean;
import be.asers.dao.EpisodeDao;
import be.asers.dao.SeasonDao;
import be.asers.dao.SeriesDao;
import be.asers.model.Episode;
import be.asers.model.Season;
import be.asers.model.Series;
import be.asers.service.FinderRemoteService;
import be.asers.service.FinderService;

/**
 * {@link Series} Finder service.
 * 
 * @author chesteric31
 */
public class FinderServiceImpl implements FinderService {

    private SimpleDateFormat dateFormat = new SimpleDateFormat(Series.DATE_PATTERN, Locale.US);
    private Context context;
    private SeriesDao seriesDao;
    private SeasonDao seasonDao;
    private EpisodeDao episodeDao;
    private FinderRemoteService remoteService;

    /**
     * Constructor.
     * 
     * @param context the {@link Context} to set
     */
    public FinderServiceImpl(Context context) {
        this.context = context;
        this.seriesDao = new SeriesDao(this.context);
        this.seasonDao = new SeasonDao(this.context);
        this.episodeDao = new EpisodeDao(this.context);
        this.remoteService = new FinderRemoteServiceImpl(this.context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SeriesBean> findMySeries() {
        List<Series> series = seriesDao.findActiveSeries();
        List<SeriesBean> beans = new ArrayList<SeriesBean>();
        if (!series.isEmpty()) {
            for (Series serie : series) {
                SeriesBean bean = mapSeries(serie);
//                bean.setCast(remoteService.createBitmap(bean));
                beans.add(bean);
            }
        }
        return beans;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeriesBean findSeries(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("The series title cannot be empty!");
        }
        Series series = seriesDao.findByTitle(title);
        if (series == null) {
            return remoteService.findSeries(title);
        } else {
            return mapSeries(series);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeriesBean addSeries(SeriesBean series) {
        SeriesBean updatedSeries = series;
        ContentValues values = mapSeriesContentValues(series);
        if (series.getId() != null) {
            seriesDao.update(values, series.getId());
        } else {
            Series addedSeries = seriesDao.add(values);
            List<SeasonBean> seasons = series.getSeasons();
            if (seasons != null) {
                for (SeasonBean season : seasons) {
                    ContentValues seasonValues = mapSeasonContentValues(addedSeries, season);
                    Season addedSeason = seasonDao.add(seasonValues);
                    addedSeason.setSeries(addedSeries);
                    List<EpisodeBean> episodes = season.getEpisodes();
                    for (EpisodeBean episode : episodes) {
                        ContentValues episodeValues = mapEpisodeContentValues(addedSeason, episode);
                        Episode addedEpisode = episodeDao.add(episodeValues);
                        addedEpisode.setSeason(addedSeason);
                        addedSeason.addEpisode(addedEpisode);
                    }
                    addedSeries.addSeason(addedSeason);
                }
                updatedSeries = mapSeries(addedSeries);
            }
        }
        return updatedSeries;
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
     * @param addedSeries the {@link Series} to use
     * @param season the {@link SeasonBean} to map
     * @return the mapped {@link ContentValues}
     */
    private ContentValues mapSeasonContentValues(Series addedSeries, SeasonBean season) {
        ContentValues seasonValues = new ContentValues();
        seasonValues.put(Season.COLUMN_NUMBER, season.getNumber());
        seasonValues.put(Season.COLUMN_SERIES, addedSeries.getId());
        return seasonValues;
    }

    /**
     * Maps a {@link SeriesBean} to a {@link ContentValues}.
     * 
     * @param series the {@link SeriesBean} to map
     * @return the mapped {@link ContentValues}
     */
    private ContentValues mapSeriesContentValues(SeriesBean series) {
        ContentValues values = new ContentValues();
        values.put(Series.COLUMN_ID, series.getId());
        values.put(Series.COLUMN_COUNTRY, series.getCountry());
        if (series.getEndDate() != null) {
            values.put(Series.COLUMN_END_DATE, dateFormat.format(series.getEndDate()));
        } else {
            values.put(Series.COLUMN_END_DATE, "");
        }
        values.put(Series.COLUMN_EPISODES_NUMBER, series.getEpisodesNumber());
        values.put(Series.COLUMN_NETWORK, series.getNetwork());
        values.put(Series.COLUMN_RUN_TIME, series.getRunTime());
        values.put(Series.COLUMN_START_DATE, dateFormat.format(series.getStartDate()));
        values.put(Series.COLUMN_TITLE, series.getTitle());
        values.put(Series.COLUMN_TV_RAGE_ID, series.getTvRageId());
        values.put(Series.COLUMN_STATUS, series.getStatus());
        values.put(Series.COLUMN_DIRECTORY, series.getDirectory());
        return values;
    }

    /**
     * Translates a {@link Series} model to a {@link SeriesBean}.
     * 
     * @param series the {@link Series} to map
     * @return the mapped {@link SeriesBean}
     */
    private SeriesBean mapSeries(Series series) {
        if (series != null) {
            SeriesBean bean = new SeriesBean();
            bean.setId(series.getId());
            bean.setCountry(series.getCountry());
            bean.setEndDate(series.getEndDate());
            bean.setEpisodesNumber(series.getEpisodesNumber());
            bean.setNetwork(series.getNetwork());
            bean.setRunTime(series.getRunTime());
            bean.setStartDate(series.getStartDate());
            bean.setTitle(series.getTitle());
            bean.setTvRageId(series.getTvRageId());
            bean.setStatus(series.getStatus());
            bean.setDirectory(series.getDirectory());
            List<SeasonBean> seasonBeans = mapSeasons(series, bean);
            bean.setSeasons(seasonBeans);
            return bean;
        } else {
            return null;
        }
    }

    /**
     * Maps {@link SeasonBean}s.
     * 
     * @param series the {@link Series} to use
     * @param bean the {@link SeriesBean} to use
     * @return the mapped {@link SeasonBean}s
     */
    private List<SeasonBean> mapSeasons(Series series, SeriesBean bean) {
        List<Season> seasons = seasonDao.findBySerieId(series.getId());
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
                seasonBean.setSeries(bean);
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
    public void addMySeries(SeriesBean series) {
        series = findSeries(series.getTitle());
        series.setStatus(Series.STATUS_ACTIVE);
        addSeries(series);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMySeries(SeriesBean series) {
        series.setStatus(Series.STATUS_INACTIVE);
        ContentValues contentValues = mapSeriesContentValues(series);
        seriesDao.update(contentValues, series.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeriesDao getSeriesDao() {
        return seriesDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpisodeBean findAirDateNextEpisode(SeriesBean series) {
        List<SeasonBean> seasons = series.getSeasons();
        if (seasons != null && !seasons.isEmpty()) {
            for (SeasonBean season : seasons) {
                List<EpisodeBean> episodes = season.getEpisodes();
                if (episodes != null && !episodes.isEmpty()) {
                    for (EpisodeBean episode : episodes) {
                        Calendar calendar = new GregorianCalendar();
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        Date today = calendar.getTime();
                        Date airDate = episode.getAirDate();
                        if (airDate != null && airDate.after(today)) {
                            EpisodeBean bean = new EpisodeBean();
                            bean.setAirDate(airDate);
                            SeasonBean seasonBean = new SeasonBean();
                            seasonBean.setSeries(series);
                            bean.setSeason(seasonBean);
                            return bean;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshSeries(SeriesBean mySerie) {
        String title = mySerie.getTitle();
        SeriesBean remoteSeries = remoteService.findSeries(title);
        Series series = seriesDao.findByTitle(title);
        SeriesBean localSeries = mapSeries(series);
        if (!localSeries.equals(remoteSeries)) {
            Long id = localSeries.getId();
            remoteSeries.setId(localSeries.getId());
            remoteSeries.setStatus(localSeries.getStatus());
            seriesDao.update(mapSeriesContentValues(remoteSeries), id);
            List<SeasonBean> localSeasons = localSeries.getSeasons();
            List<SeasonBean> remoteSeasons = remoteSeries.getSeasons();
            refreshSeasons(series, localSeasons, remoteSeasons);
        }
    }

    /**
     * Refresh seasons.
     *
     * @param series the series
     * @param localSeasons the local seasons
     * @param remoteSeasons the remote seasons
     */
    private void refreshSeasons(Series series, List<SeasonBean> localSeasons, List<SeasonBean> remoteSeasons) {
        if (!localSeasons.equals(remoteSeasons)) {
            int remoteSize = remoteSeasons.size();
            int localSize = localSeasons.size();
            for (int i = 0; i < remoteSize; i++) {
                SeasonBean remoteSeason = remoteSeasons.get(i);
                boolean found = false;
                for (int j = i; j < localSize; j++) {
                    SeasonBean localSeason = localSeasons.get(j);
                    if (remoteSeason.getNumber().equals(localSeason.getNumber())) {
                        refreshSeason(series, remoteSeason, localSeason);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    seasonDao.add(mapSeasonContentValues(series, remoteSeason));
                }
            }
        }
    }

    /**
     * Refresh season.
     *
     * @param series the series
     * @param remoteSeason the remote season
     * @param localSeason the local season
     */
    private void refreshSeason(Series series, SeasonBean remoteSeason, SeasonBean localSeason) {
        if (!remoteSeason.equals(localSeason)) {
            List<EpisodeBean> remoteEpisodes = remoteSeason.getEpisodes();
            List<EpisodeBean> localEpisodes = localSeason.getEpisodes();
            Long seasonId = localSeason.getId();
            seasonDao.update(mapSeasonContentValues(series, remoteSeason), seasonId);
            if (!localEpisodes.equals(remoteEpisodes)) {
                Season season = seasonDao.findById(seasonId);
                int remoteSize = remoteEpisodes.size();
                int localSize = localEpisodes.size();
                if (remoteSize != localSize) {
                    for (int i = 0; i < remoteSize; i++) {
                        EpisodeBean remoteEpisode = remoteEpisodes.get(i);
                        boolean found = false;
                        for (int j = i; j < localSize; j++) {
                            EpisodeBean localEpisode = localEpisodes.get(j);
                            if (remoteEpisode.getNumber().equals(localEpisode.getNumber())) {
                                refreshEpisode(season, remoteEpisode, localEpisode);
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            episodeDao.add(mapEpisodeContentValues(season, remoteEpisode));
                        }
                    }
                }
            }
        }
    }

    /**
     * Refresh episode.
     *
     * @param season the season
     * @param remoteEpisode the remote episode
     * @param localEpisode the local episode
     */
    private void refreshEpisode(Season season, EpisodeBean remoteEpisode, EpisodeBean localEpisode) {
        if (!remoteEpisode.equals(localEpisode)) {
            episodeDao.update(mapEpisodeContentValues(season, remoteEpisode), localEpisode.getId());
        }
    }

}
