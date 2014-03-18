package be.asers.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;
import be.asers.bean.EpisodeBean;
import be.asers.bean.SeasonBean;
import be.asers.bean.SeriesBean;
import be.asers.model.Episode;
import be.asers.model.Series;
import be.asers.service.FinderRemoteService;

/**
 * {@link Series} Finder remote (from epguides.com) service.
 * 
 * @author chesteric31
 */
public class FinderRemoteServiceImpl implements FinderRemoteService {

    private static final String CSV_DELIMITER = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
    private static final String MIN = "min";
    private static final String EPS = "eps";
    private static final String DOUBLE_QUOTES = "\"";
    private static final String EMPTY_STRING = "";
    private static final String EPGUIDES_URL = "http://epguides.com/";
    private static final String ALL_SERIES_URL = EPGUIDES_URL + "common/allshows.txt";
    private static final String CSV_EXPORT_URL = EPGUIDES_URL + "common/exportToCSV.asp?rage=";
    private static final String END_DATA_DELIMITER = "</pre>";
    private static final int HEADER_NUMBER_LINES = 7;
    private static final String FIRST_COLUMN_TITLE = "number";
    private static final String NOT_SPECIAL_EPISODE = "n";
    private Context context;

    /**
     * Constructor.
     * 
     * @param context the {@link Context} to set
     */
    public FinderRemoteServiceImpl(Context context) {
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeriesBean findSeries(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("The series title cannot be empty!");
        }
        return findByTitle(title);
    }

    /**
     * Finds the series from the epguides.com site.
     * 
     * @param title the title for the series to find
     * @return the found {@link SeriesBean}, null, if none found
     */
    private SeriesBean findByTitle(String title) {
        BufferedReader bufferedReader = createReader(null);
        List<String> contents = createStringsContent(bufferedReader);
        for (String content : contents) {
            String[] tokens = content.split(CSV_DELIMITER);
            if (tokens[0].equals("\"" + title + "\"")) {
                SeriesBean bean = buildSeries(tokens);
                try {
                    return buildDetails(bean);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public SeriesBean buildSeries(String[] tokens) {
        SeriesBean series = new SeriesBean();
        series.setTitle(tokens[0].replaceAll(DOUBLE_QUOTES, EMPTY_STRING));
        int j = 1;
        series.setDirectory(tokens[j]);
        j++;
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
        series.setCountry(tokens[j]);
        return series;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeriesBean buildSkinnySeries(String[] tokens) {
        SeriesBean series = new SeriesBean();
        series.setTitle(tokens[0].replaceAll(DOUBLE_QUOTES, EMPTY_STRING));
        series.setNetwork(tokens[7].replaceAll(DOUBLE_QUOTES, EMPTY_STRING));
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

    /**
     * Builds a {@link SeriesBean} with episodes from {@link SeriesBean}.
     * 
     * @param series the {@link SeriesBean} to use
     * @return the built {@link SeriesBean}
     * @throws MalformedURLException if an error occurred
     */
    private SeriesBean buildDetails(SeriesBean series) throws MalformedURLException {
        URL url = new URL(CSV_EXPORT_URL + series.getTvRageId());
        BufferedReader reader = createReader(url);
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
    public BufferedReader createReader(URL url) {
        URLConnection connection;
        BufferedReader reader = null;
        try {
            if (url == null) {
                url = new URL(ALL_SERIES_URL);
            }
            connection = url.openConnection();
            checkProxy(connection);
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return reader;
    }

    /**
     * Checks proxy preferences and in case of, use them.
     * 
     * @param connection the {@link URLConnection} to use
     */
    private void checkProxy(URLConnection connection) {
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
        String unaired = "UNAIRED";
        unaired = "\"" + unaired + "\"";
        if (!unaired.equals(date) && !"UNKNOWN".equals(date) && !"UNAIRED".equals(date)) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Episode.DATE_PATTERN, Locale.US);
                date = date.replaceAll("\"", "");
                Date airDate = simpleDateFormat.parse(date);
                episode.setAirDate(airDate);
            } catch (ParseException e) {
                System.err.println(e.getLocalizedMessage());
                // throw new RuntimeException(e);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Bitmap createBitmap(SeriesBean series) {
        if (series == null || series.getDirectory() == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }
        Bitmap bitmap = null;
        String castUrl = EPGUIDES_URL + series.getDirectory() + "/cast.jpg";
        URLConnection connection;
        try {
            connection = new URL(castUrl).openConnection();
            checkProxy(connection);
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            bitmap = Bitmap.createScaledBitmap(bitmap, 125, 100, true);
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
//            throw new RuntimeException(e);
        }
        return bitmap;
    }
}
