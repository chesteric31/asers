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

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import be.asers.bean.EpisodeBean;
import be.asers.bean.SeasonBean;
import be.asers.bean.SeriesBean;
import be.asers.dao.EpisodeDao;
import be.asers.dao.SeasonDao;
import be.asers.dao.SeriesDao;
import be.asers.model.Episode;
import be.asers.model.Season;
import be.asers.model.Series;

/**
 * {@link Series} Finder service.
 * 
 * @author chesteric31
 */
public class FinderServiceImpl implements FinderService {

    private static final String CSV_DELIMITER = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
    private static final String MIN = "min";
    private static final String EPS = "eps";
    private static final String DOUBLE_QUOTES = "\"";
    private static final String EMPTY_STRING = "";
    private static final String EPGUIDES_URL = "http://epguides.com/";
    private static final String ALL_SERIES_URL = EPGUIDES_URL + "common/allshows.txt";
    private static final String CSV_EXPORT_URL = EPGUIDES_URL + "common/exportToCSV.asp?rage=";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(Series.DATE_PATTERN, Locale.US);
    private Context context;
    private SeriesDao seriesDao;
    private SeasonDao seasonDao;
    private EpisodeDao episodeDao;

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
            return findOnEpguides(title);
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
    private SeriesBean findOnEpguides(String title) {
        BufferedReader bufferedReader = createReader(null);
        List<String> contents = createStringsContent(bufferedReader);
        for (String content : contents) {
            String[] tokens = content.split(CSV_DELIMITER);
            if (tokens[0].equals("\"" + title + "\"")) {
                // if (token.matches("(.*)" + title + "(.*)")) {
                SeriesBean bean = buildSeries(tokens);
                return addSeries(bean);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeriesBean addSeries(SeriesBean series) {
        ContentValues values = mapSeriesContentValues(series);
        if (series.getId() != null) {
            seriesDao.update(values, series.getId());
            return series;
        } else {
            Series addedSeries = seriesDao.add(values);
            List<SeasonBean> seasons = series.getSeasons();
            for (SeasonBean season : seasons) {
                ContentValues seasonValues = mapSeasonContentValues(addedSeries, season);
                Season addedSeason = seasonDao.add(seasonValues);
                addedSeason.setSeries(addedSeries);
                List<EpisodeBean> episodes = season.getEpisodes();
                for (EpisodeBean episode : episodes) {
                    ContentValues episodeValues = mapEpisodeContentValues(addedSeason, episode);
                    Episode addedEpisode = episodeDao.add(episodeValues);
                    addedEpisode.setSeason(addedSeason);
                }
            }
            return mapSeries(addedSeries);
        }
        
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
            episodeValues.put(Episode.COLUMN_AIR_DATE, dateFormat.format(airDate));
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
        return values;
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
            bean.setId(model.getId());
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
     * {@inheritDoc}
     */
    public SeriesBean buildSeries(String[] tokens) {
        SeriesBean series = new SeriesBean();
        series.setTitle(tokens[0].replaceAll(DOUBLE_QUOTES, EMPTY_STRING));
        int j = 2;
        buildTvRageId(tokens[j], series);
        j++;
        j = processStartEndDates(tokens, series, j);
        j++;
        buildEpisodeNumbers(tokens[j], series);
        j++;
        buildRuntime(tokens[j], series);
        j++;
        buildNetwork(tokens[j], series);
        j++;
        buildCountry(tokens[j], series);
        return series;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeriesBean buildSkinnySeries(String[] tokens) {
        SeriesBean series = new SeriesBean();
        series.setTitle(tokens[0].replaceAll(DOUBLE_QUOTES, EMPTY_STRING));
        return series;
    }

    /**
     * Builds the TV rage id.
     * 
     * @param token the token with data
     * @param series the {@link SeriesBean} to update
     */
    private void buildTvRageId(String token, SeriesBean series) {
        if (isNumeric(token)) {
            series.setTvRageId(Integer.valueOf(token));
        }
    }

    /**
     * Builds the country.
     * 
     * @param token the token with data
     * @param series the {@link SeriesBean} to update
     */
    private void buildCountry(String token, SeriesBean series) {
        series.setCountry(token);
    }

    /**
     * Builds the network.
     * 
     * @param token the token with data
     * @param series the {@link SeriesBean} to update
     */
    private void buildNetwork(String token, SeriesBean series) {
        token = token.replaceAll(DOUBLE_QUOTES, EMPTY_STRING);
        token = token.trim();
        series.setNetwork(token);
    }

    /**
     * Builds the runtime.
     * 
     * @param token the token with data
     * @param series the {@link SeriesBean} to update
     */
    private void buildRuntime(String token, SeriesBean series) {
        token = token.replaceAll(DOUBLE_QUOTES, EMPTY_STRING);
        token = token.replaceAll(MIN, EMPTY_STRING);
        token = token.trim();
        if (isNumeric(token)) {
            series.setRunTime(Integer.valueOf(token));
        }
    }

    /**
     * Builds the episode numbers.
     * 
     * @param token the token with data
     * @param series the {@link SeriesBean} to update
     */
    private void buildEpisodeNumbers(String token, SeriesBean series) {
        token = token.replaceAll(DOUBLE_QUOTES, EMPTY_STRING);
        token = token.replaceAll(EPS, EMPTY_STRING);
        token = token.replaceAll("\\?", EMPTY_STRING);
        token = token.trim();
        if (isNumeric(token)) {
            series.setEpisodesNumber(Integer.valueOf(token));
        }
    }

    /**
     * Builds the start/end dates.
     * 
     * @param tokens the tokens with data
     * @param series the {@link SeriesBean} to update
     * @param j the counter to use
     * @return the updated counter j
     */
    private int processStartEndDates(String[] tokens, SeriesBean series, int j) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Series.DATE_PATTERN, Locale.US);
        Date startDate;
        SimpleDateFormat simpleDateFormatOptional = new SimpleDateFormat(Series.DATE_PATTERN_OPTIONAL, Locale.US);
        if (!EMPTY_STRING.equals(tokens[j])) {
            tokens[j] = tokens[j].replaceAll("\\?", EMPTY_STRING);
            if (!EMPTY_STRING.equals(tokens[j])) {
                try {
                    startDate = simpleDateFormat.parse(tokens[j]);
                } catch (ParseException e) {
                    try {
                        startDate = simpleDateFormatOptional.parse(tokens[j]);
                    } catch (ParseException e1) {
                        throw new RuntimeException(e1);
                    }
                }
                series.setStartDate(startDate);
            }
        }
        j++;
        if (!Series.NO_KNOWN_END_DATE.equals(tokens[j]) && !EMPTY_STRING.equals(tokens[j])) {
            tokens[j] = tokens[j].replaceAll("\\?", EMPTY_STRING);
            tokens[j] = tokens[j].replaceAll("see NOTES", EMPTY_STRING);
            if (!EMPTY_STRING.equals(tokens[j])) {
                Date endDate;
                try {
                    if (tokens[j].contains("Mat")) {
                        tokens[j] = tokens[j].replace("Mat", "Mar");
                    }
                    if (tokens[j].contains("Sept")) {
                        tokens[j] = tokens[j].replace("Sept", "Sep");
                    }
                    if (tokens[j].contains("___")) {
                        tokens[j] = tokens[j].replace("___", EMPTY_STRING);
                    }
                    endDate = simpleDateFormat.parse(tokens[j]);
                } catch (ParseException e) {
                    try {
                        endDate = simpleDateFormatOptional.parse(tokens[j]);
                    } catch (ParseException e1) {
                        throw new RuntimeException(e1);
                    }
                }
                series.setEndDate(endDate);
            }
        }
        return j;
    }

    /**
     * Checks if a String is numeric.
     * 
     * @param string the String to check
     * @return true if the all the character of the String is digit, false
     *         otherwise
     */
    private boolean isNumeric(String string) {
        if (string == null || string.isEmpty()) {
            return false;
        }
        for (int i = 0; i < string.length(); i++) {
            if (!Character.isDigit(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static final String END_DATA_DELIMITER = "</pre>";
    private static final int HEADER_NUMBER_LINES = 7;
    private static final String FIRST_COLUMN_TITLE = "number";
    private static final String NOT_SPECIAL_EPISODE = "n";

    /**
     * {@inheritDoc}
     */
    @Override
    public SeriesBean findSeriesDetails(String title) throws IOException {
        SeriesBean series = findSeries(title);
        URL url = new URL(CSV_EXPORT_URL + series.getTvRageId());
        BufferedReader reader = createReader(url);
        // try {
        // new ReaderSeasonTask(series).execute(reader).get();
        // } catch (InterruptedException e) {
        // throw new RuntimeException(e);
        // } catch (ExecutionException e) {
        // throw new RuntimeException(e);
        // }
        String line = null;
        boolean skipFurtherLines = false;
        int i = 0;
        SeasonBean lastSeason = new SeasonBean();
        lastSeason.setSeries(series);
        lastSeason.setNumber(0);
        try {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return series;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeriesBean addMySeries(SeriesBean series) {
        try {
            series = findSeriesDetails(series.getTitle());
            series.setStatus(Series.STATUS_ACTIVE);
            return addSeries(series);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public BufferedReader createReader(URL url) {
        URLConnection connection;
        BufferedReader reader = null;
        try {
            if (url == null) {
                url = new URL(ALL_SERIES_URL);
            }
            connection = url.openConnection();
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
            reader = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return reader;
    }

    /**
     * {@inheritDoc}
     */
    public List<String> createStringsContent(BufferedReader bufferedReader) {
        List<String> strings = new ArrayList<String>();
        try {
            String line = null;
            boolean firstLineToSkip = true;
            while ((line = bufferedReader.readLine()) != null) {
                if (firstLineToSkip) {
                    firstLineToSkip = false;
                } else {
                    line = line.trim();
                    strings.add(line);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return strings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeriesDao getSeriesDao() {
        return seriesDao;
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
            String[] strings = line.split(CSV_DELIMITER);
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
        String date = strings[4];
        if (!"UNAIRED".equals(date)) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Episode.DATE_PATTERN, Locale.US);
                date = date.replaceAll("\"", "");
                Date airDate = simpleDateFormat.parse(date);
                episode.setAirDate(airDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
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

}
