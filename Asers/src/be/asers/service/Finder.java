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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import be.asers.model.Episode;
import be.asers.model.Season;
import be.asers.model.Series;

/**
 * {@link Series} Finder service. 
 *
 * @author chesteric31
 */
public class Finder {

    private static final String END_DATA_DELIMITER = "</pre>";
    private static final String NOT_SPECIAL_EPISODE = "n";
    private static final String EPISODE_DATE_PATTERN = "dd/MMM/yy";
    private static final String FIRST_COLUMN_TITLE = "number";
    private static final String NEW_LINE = "\n";
    private static final String COMMA = ",";
    private static final String SERIES_DATE_PATTERN = "MMM yyyy";
    private static final String MIN = "min";
    private static final String EPS = "eps";
    private static final String DOUBLE_QUOTES = "\"";
    private static final String EMPTY_STRING = "";
    private static final int HEADER_NUMBER_LINES = 7;
    private static final String EPGUIDES_URL = "http://epguides.com/";
    private static final String ALL_SERIES_URL = EPGUIDES_URL + "common/allshows.txt";
    private static final String CSV_EXPORT_URL = EPGUIDES_URL + "common/exportToCSV.asp?rage=";
    private Context context;

    /**
     * Constructor.
     * 
     * @param context the {@link Context} to set
     */
    public Finder(Context context) {
        this.context = context;
    }
    
    /**
     * Finds the series following the title criteria.
     * 
     * @param title the title to use
     * @return the found {@link Series} entity
     */
    public Series findSeries(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("The series title cannot be empty!");
        }
        String content;
        try {
            URL url = new URL(ALL_SERIES_URL);
            content = retrieveBasicUrlContent(url);
            String[] tokens = content.split(COMMA);
            int i = 0;
            for (String token : tokens) {
                if (token.equalsIgnoreCase(title)) {
                    return buildSeries(tokens, i, token);
                }
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Builds a {@link Series} object from the tokens.
     * 
     * @param tokens the tokens to use
     * @param i the counter to use
     * @param token the token to use
     * @return the build {@link Series}
     * @throws ParseException if the parse failed
     */
    private Series buildSeries(String[] tokens, int i, String token) throws ParseException {
        Series series = new Series();
        series.setTitle(token);
        int j = i + 1;
        series.setTvRageId(Integer.valueOf(tokens[j]));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SERIES_DATE_PATTERN, Locale.US);
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
     * Retrieves a basic URL content from an {@link URL} object.
     * 
     * @param url the URL to use
     * @return the String basic URL content
     * @throws IOException if an error occurred
     */
    public String retrieveBasicUrlContent(URL url) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("The url cannot be empty!");
        }
        BufferedReader reader = retrieveReaderFromUrl(url);
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            stringBuilder.append(line);
            stringBuilder.append(NEW_LINE);
        }
        reader.close();
        return stringBuilder.toString();
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
     * Finds the {@link Series} details following the title criteria.
     * 
     * @param title the title to use
     * @return the found {@link Series} entity
     * @throws IOException if an error occurred
     */
    public Series findSeriesDetails(String title) throws IOException {
        Series series = findSeries(title);
        URL url = new URL(CSV_EXPORT_URL + series.getTvRageId());
        BufferedReader reader = retrieveReaderFromUrl(url);
        String line = null;
        boolean skipFurtherLines = false;
        int i = 0;
        Season lastSeason = new Season();
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
     * @param series the {@link Series} to use
     * @param line the line to process
     * @param skipNextLines if we must skip the next lines
     * @param lastSeason the last {@link Season} processed
     * @return the {@link Season} entity
     */
    private Season processEpisodes(Series series, String line, boolean skipNextLines, Season lastSeason) {
        if (!skipNextLines && !line.startsWith(FIRST_COLUMN_TITLE)) {
            String[] strings = line.split(COMMA);
            Episode episode = buildEpisode(strings);
            Integer seasonNumber = Integer.valueOf(strings[1]);
            if (Boolean.TRUE.equals(episode.getSpecial())) {
                specialEpisodeProcess(series, episode, seasonNumber);
            } else {
                if (lastSeason.getNumber() != seasonNumber) {
                    lastSeason = buildSeason(series, seasonNumber);
                }
                List<Episode> episodes = lastSeason.getEpisodes();
                if (episodes == null) {
                    episodes = new ArrayList<Episode>();
                }
                episodes.add(episode);
                lastSeason.setEpisodes(episodes);
                episode.setSeason(lastSeason);
            }
        }
        return lastSeason;
    }

    /**
     * Builds the {@link Season} from the {@link Series} and the {@link Season} number.
     * 
     * @param series the {@link Series} to use
     * @param seasonNumber the {@link Season} number to use
     * @return the build {@link Season}
     */
    private Season buildSeason(Series series, Integer seasonNumber) {
        Season lastSeason;
        List<Season> seasons = series.getSeasons();
        if (seasons == null) {
            seasons = new ArrayList<Season>();
        }
        lastSeason = new Season();
        lastSeason.setNumber(seasonNumber);
        lastSeason.setSeries(series);
        seasons.add(lastSeason);
        series.setSeasons(seasons);
        return lastSeason;
    }

    /**
     * Special {@link Episode} process.
     * 
     * @param series the {@link Series} to use
     * @param episode the {@link Episode} to process
     * @param seasonNumber the {@link Season} number to use
     */
    private void specialEpisodeProcess(Series series, Episode episode, Integer seasonNumber) {
        List<Season> seasons = series.getSeasons();
        for (Season season : seasons) {
            if (seasonNumber.equals(season.getNumber())) {
                episode.setSeason(season);
                season.getEpisodes().add(episode);
            }
        }
    }

    /**
     * Builds the {@link Episode} from the String array.
     * 
     * @param strings the String array contained all the {@link Episode} informations
     * @return the built {@link Episode}
     */
    private Episode buildEpisode(String[] strings) {
        Episode episode = new Episode();
        if (!strings[0].isEmpty()) {
            episode.setNumber(Integer.valueOf(strings[0]));
        }
        if (!strings[2].isEmpty()) {
            episode.setEpisode(Integer.valueOf(strings[2]));
        }
        if (!strings[3].isEmpty()) {
            episode.setProductionCode(strings[3]);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(EPISODE_DATE_PATTERN, Locale.US); 
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

}
