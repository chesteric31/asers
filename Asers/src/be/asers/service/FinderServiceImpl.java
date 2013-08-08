package be.asers.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import be.asers.bean.EpisodeBean;
import be.asers.bean.SeasonBean;
import be.asers.bean.SeriesBean;
import be.asers.dao.SeriesDao;
import be.asers.model.Episode;
import be.asers.model.Series;

/**
 * {@link Series} Finder service.
 * 
 * @author chesteric31
 */
public class FinderServiceImpl implements FinderService {

    private static final String END_DATA_DELIMITER = "</pre>";
    private static final String NOT_SPECIAL_EPISODE = "n";
    private static final String FIRST_COLUMN_TITLE = "number";
    private static final String NEW_LINE = "\n";
    private static final String COMMA = ",";
    private static final String MIN = "min";
    private static final String EPS = "eps";
    private static final String DOUBLE_QUOTES = "\"";
    private static final String EMPTY_STRING = "";
    private static final int HEADER_NUMBER_LINES = 7;
    private static final String EPGUIDES_URL = "http://epguides.com/";
    private static final String ALL_SERIES_URL = EPGUIDES_URL + "common/allshows.txt";
    private static final String CSV_EXPORT_URL = EPGUIDES_URL + "common/exportToCSV.asp?rage=";
    private Context context;

    private SeriesDao seriesDao;

    /**
     * Constructor.
     * 
     * @param context the {@link Context} to set
     */
    public FinderServiceImpl(Context context) {
        this.context = context;
        this.seriesDao = new SeriesDao(this.context);
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
                beans.add(mapSeries(serie));        
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
            return findInInternet(title);
        } else {
            return mapSeries(series);
        }
    }

    /**
     * Finds the series from the epguides.com site.
     * 
     * @param title the title for the series to find
     * @return the found {@link SeriesBean}, null, if none found
     */
    private SeriesBean findInInternet(String title) {
        String content;
        try {
            URL url = new URL(ALL_SERIES_URL);
            content = new UrlRetrieverTask(this.context).execute(url).get();
            String[] tokens = content.split(COMMA);
            int i = 0;
            for (String token : tokens) {
                // if (token.matches("(.*)" + title + "(.*)")) {
                if (token.equalsIgnoreCase(title)) {
                    SeriesBean bean = buildSeries(tokens, i, token);
                    return addSeries(bean);
                }
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeriesBean addSeries(SeriesBean series) {
        ContentValues values = new ContentValues();
        values.put(Series.COLUMN_COUNTRY, series.getCountry());
        SimpleDateFormat dateFormat = new SimpleDateFormat(Series.DATE_PATTERN, Locale.US);
        values.put(Series.COLUMN_END_DATE, dateFormat.format(series.getEndDate()));
        values.put(Series.COLUMN_EPISODES_NUMBER, series.getEpisodesNumber());
        values.put(Series.COLUMN_NETWORK, series.getNetwork());
        values.put(Series.COLUMN_RUN_TIME, series.getRunTime());
        values.put(Series.COLUMN_START_DATE, dateFormat.format(series.getStartDate()));
        values.put(Series.COLUMN_TITLE, series.getTitle());
        values.put(Series.COLUMN_TV_RAGE_ID, series.getTvRageId());
        values.put(Series.COLUMN_STATUS, series.getStatus());
        Series model = seriesDao.add(values);
        return mapSeries(model);
    }

    /**
     * Translates a {@link Series} model to a {@link SeriesBean}.
     * 
     * @param model the {@link Series} to map
     * @return the mapped {@link SeriesBean}
     */
    private SeriesBean mapSeries(Series model) {
        if (model != null) {
            SeriesBean bean = new SeriesBean();
            bean.setCountry(model.getCountry());
            bean.setEndDate(model.getEndDate());
            bean.setEpisodesNumber(model.getEpisodesNumber());
            bean.setNetwork(model.getNetwork());
            bean.setRunTime(model.getRunTime());
            bean.setStartDate(model.getStartDate());
            bean.setTitle(model.getTitle());
            bean.setTvRageId(model.getTvRageId());
            bean.setStatus(model.getStatus());
            return bean;
        } else {
            return null;
        }
    }

    /**
     * Builds a {@link SeriesBean} from the tokens.
     * 
     * @param tokens the tokens to use
     * @param i the counter to use
     * @param token the token to use
     * @return the built {@link SeriesBean}
     * @throws ParseException if the parse failed
     */
    private SeriesBean buildSeries(String[] tokens, int i, String token) throws ParseException {
        SeriesBean series = new SeriesBean();
        series.setTitle(token);
        int j = i + 1;
        series.setTvRageId(Integer.valueOf(tokens[j]));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Series.DATE_PATTERN, Locale.US);
        j++;
        Date startDate = simpleDateFormat.parse(tokens[j]);
        series.setStartDate(startDate);
        j++;
        Date endDate = simpleDateFormat.parse(tokens[j]);
        series.setEndDate(endDate);
        j++;
        String episodesNumber = tokens[j];
        episodesNumber = episodesNumber.replaceAll(DOUBLE_QUOTES, EMPTY_STRING);
        episodesNumber = episodesNumber.replaceAll(EPS, EMPTY_STRING);
        episodesNumber = episodesNumber.trim();
        series.setEpisodesNumber(Integer.valueOf(episodesNumber));
        j++;
        String runTime = tokens[j];
        runTime = runTime.replaceAll(DOUBLE_QUOTES, EMPTY_STRING);
        runTime = runTime.replaceAll(MIN, EMPTY_STRING);
        runTime = runTime.trim();
        series.setRunTime(Integer.valueOf(runTime));
        j++;
        String network = tokens[j];
        network = network.replaceAll(DOUBLE_QUOTES, EMPTY_STRING);
        network = network.trim();
        series.setNetwork(network);
        j++;
        String country = tokens[j];
        int indexOf = country.indexOf(NEW_LINE);
        country = country.substring(0, indexOf);
        series.setCountry(country);
        return series;
    }

    /**
     * Retrieves a {@link BufferedReader} from an URL object.
     * 
     * @param url the URL to use
     * @return the {@link BufferedReader} URL content
     * @throws IOException if an error occurred
     */
    private BufferedReader retrieveReaderFromUrl(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        boolean isProxy = preferences.getBoolean("isProxy", false);
        if (isProxy) {
            boolean isProxyAuthentication = preferences.getBoolean("isProxyAuthentication", false);
            if (isProxyAuthentication) {
                String user = preferences.getString("proxyUser", "");
                String password = preferences.getString("proxyPassword", "");
                String userPassword = user + ":" + password;
                String encoded = Base64.encodeToString(userPassword.getBytes(), 0);
                connection.setRequestProperty("Proxy-Authorization", "Basic " + encoded);
            }
        }
        InputStream inputStream = connection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        return reader;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeriesBean findSeriesDetails(String title) throws IOException {
        SeriesBean series = findSeries(title);
        URL url = new URL(CSV_EXPORT_URL + series.getTvRageId());
        BufferedReader reader = retrieveReaderFromUrl(url);
        String line = null;
        boolean skipFurtherLines = false;
        int i = 0;
        SeasonBean lastSeason = new SeasonBean();
        lastSeason.setSeries(series);
        lastSeason.setNumber(0);
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (i < HEADER_NUMBER_LINES) {
                i++;
                continue;
            }
            if (line.startsWith(END_DATA_DELIMITER)) {
                skipFurtherLines = true;
            }
            lastSeason = processEpisodes(series, line, skipFurtherLines, lastSeason);
        }
        return series;
    }

    /**
     * Process the episodes.
     * 
     * @param series the {@link SeriesBean} to use
     * @param line the line to process
     * @param skipNextLines if we must skip the next lines
     * @param lastSeason the last {@link SeasonBean} processed
     * @return the {@link SeasonBean} entity
     */
    private SeasonBean processEpisodes(SeriesBean series, String line, boolean skipNextLines, SeasonBean lastSeason) {
        if (!skipNextLines && !line.startsWith(FIRST_COLUMN_TITLE)) {
            String[] strings = line.split(COMMA);
            EpisodeBean episode = buildEpisode(strings);
            Integer seasonNumber = Integer.valueOf(strings[1]);
            if (Boolean.TRUE.equals(episode.getSpecial())) {
                specialEpisodeProcess(series, episode, seasonNumber);
            } else {
                if (lastSeason.getNumber() != seasonNumber) {
                    lastSeason = buildSeason(series, seasonNumber);
                }
                List<EpisodeBean> episodes = lastSeason.getEpisodes();
                if (episodes == null) {
                    episodes = new ArrayList<EpisodeBean>();
                }
                episodes.add(episode);
                lastSeason.setEpisodes(episodes);
                episode.setSeason(lastSeason);
            }
        }
        return lastSeason;
    }

    /**
     * Builds the {@link SeasonBean} from the {@link SeriesBean} and the
     * {@link SeasonBean} number.
     * 
     * @param series the {@link SeriesBean} to use
     * @param seasonNumber the {@link SeasonBean} number to use
     * @return the build {@link SeasonBean}
     */
    private SeasonBean buildSeason(SeriesBean series, Integer seasonNumber) {
        SeasonBean lastSeason;
        List<SeasonBean> seasons = series.getSeasons();
        if (seasons == null) {
            seasons = new ArrayList<SeasonBean>();
        }
        lastSeason = new SeasonBean();
        lastSeason.setNumber(seasonNumber);
        lastSeason.setSeries(series);
        seasons.add(lastSeason);
        series.setSeasons(seasons);
        return lastSeason;
    }

    /**
     * Special {@link EpisodeBean} process.
     * 
     * @param series the {@link SeriesBean} to use
     * @param episode the {@link EpisodeBean} to process
     * @param seasonNumber the {@link SeasonBean} number to use
     */
    private void specialEpisodeProcess(SeriesBean series, EpisodeBean episode, Integer seasonNumber) {
        List<SeasonBean> seasons = series.getSeasons();
        for (SeasonBean season : seasons) {
            if (seasonNumber.equals(season.getNumber())) {
                episode.setSeason(season);
                season.getEpisodes().add(episode);
            }
        }
    }

    /**
     * Builds the {@link EpisodeBean} from the String array.
     * 
     * @param strings the String array contained all the {@link EpisodeBean}
     *            informations
     * @return the built {@link EpisodeBean}
     */
    private EpisodeBean buildEpisode(String[] strings) {
        EpisodeBean episode = new EpisodeBean();
        if (!strings[0].isEmpty()) {
            episode.setNumber(Integer.valueOf(strings[0]));
        }
        if (!strings[2].isEmpty()) {
            episode.setEpisode(Integer.valueOf(strings[2]));
        }
        if (!strings[3].isEmpty()) {
            episode.setProductionCode(strings[3]);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Episode.DATE_PATTERN, Locale.US);
        try {
            String date = strings[4];
            date = date.replaceAll(DOUBLE_QUOTES, EMPTY_STRING);
            Date airDate = simpleDateFormat.parse(date);
            episode.setAirDate(airDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        episode.setTitle(strings[5]);
        boolean notSpecial = NOT_SPECIAL_EPISODE.equals(strings[6]);
        if (notSpecial) {
            episode.setSpecial(Boolean.FALSE);
        } else {
            episode.setSpecial(Boolean.TRUE);
        }
        episode.setTvRageLink(strings[7]);
        return episode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeriesDao getSeriesDao() {
        return seriesDao;
    }

}
