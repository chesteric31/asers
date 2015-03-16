package be.asers.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    /** The Constant CSV_DELIMITER. */
    private static final String CSV_DELIMITER = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
    
    /** The Constant MIN. */
    private static final String MIN = "min";
    
    /** The Constant EPS. */
    private static final String EPS = "eps";
    
    /** The Constant DOUBLE_QUOTES. */
    private static final String DOUBLE_QUOTES = "\"";
    
    /** The Constant EMPTY_STRING. */
    private static final String EMPTY_STRING = "";
    
    /** The Constant EPGUIDES_URL. */
    private static final String EPGUIDES_URL = "http://epguides.com/";
    
    /** The Constant ALL_SERIES_URL. */
    private static final String ALL_SERIES_URL = EPGUIDES_URL + "common/allshows.txt";
    
    /** The Constant CSV_EXPORT_URL. */
    private static final String CSV_EXPORT_URL = EPGUIDES_URL + "common/exportToCSV.asp?rage=";
    
    /** The Constant END_DATA_DELIMITER. */
    private static final String END_DATA_DELIMITER = "</pre>";
    
    /** The Constant HEADER_NUMBER_LINES. */
    private static final int HEADER_NUMBER_LINES = 7;
    
    /** The Constant FIRST_COLUMN_TITLE. */
    private static final String FIRST_COLUMN_TITLE = "number";
    
    /** The Constant NOT_SPECIAL_EPISODE. */
    private static final String NOT_SPECIAL_EPISODE = "n";
    
    /** The context. */
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
        BufferedReader reader = createReader(CSV_EXPORT_URL + series.getTvRageId());
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
    public BufferedReader createReader(String url) {
        InputStream inputStream = null;
        if (url == null) {
            inputStream = buildStream(buildHttpClient(), ALL_SERIES_URL);
        } else {
            inputStream = buildStream(buildHttpClient(), url);
        }
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }
   
    /**
     * Creates the input stream.
     *
     * @param url the url
     * @return the input stream
     */
    private InputStream createInputStream(String url) {
      if (url == null) {
          return buildStream(buildHttpClient(), ALL_SERIES_URL);
      } else {
          return buildStream(buildHttpClient(), url);
      }
  }

    /**
     * Builds the http client.
     * 
     * @return the default http client
     */
    private DefaultHttpClient buildHttpClient() {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 15000);
        HttpConnectionParams.setSoTimeout(params, 10000);
        BasicCredentialsProvider credentialsProvider = buildCredentialsProvider();
        client.setCredentialsProvider(credentialsProvider);
        return client;
    }

    /**
     * Builds the stream.
     * 
     * @param client the client
     * @param url the url
     * @return the input stream
     */
    private InputStream buildStream(HttpClient client, String url) {
        HttpGet get = new HttpGet(url);
        HttpResponse response;
        try {
            response = client.execute(get);
            HttpEntity entity = response.getEntity();
            return entity.getContent();
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Builds the credentials provider.
     * 
     * @return the basic credentials provider
     */
    private BasicCredentialsProvider buildCredentialsProvider() {
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        Credentials credentials = new UsernamePasswordCredentials("bprtester", "bprtester");
        credentialsProvider.setCredentials(new AuthScope("10.16.0.25", AuthScope.ANY_PORT), credentials);
        return credentialsProvider;
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
            if (episode != null) {
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
        EpisodeBean episode = null;
        if (!strings[0].isEmpty()) {
            episode = new EpisodeBean();
            episode.setNumber(Integer.valueOf(strings[0]));
            if (!strings[2].isEmpty()) {
                episode.setEpisode(Integer.valueOf(strings[2]));
            }
            if (!strings[3].isEmpty()) {
                episode.setProductionCode(strings[3]);
            }
            String date = strings[4];
            setAirDate(episode, date);
            episode.setTitle(strings[5]);
            boolean notSpecial = NOT_SPECIAL_EPISODE.equals(strings[6]);
            if (notSpecial) {
                episode.setSpecial(Boolean.FALSE);
            } else {
                episode.setSpecial(Boolean.TRUE);
            }
            episode.setTvRageLink(strings[7]);
        }
        return episode;
    }

    /**
     * Sets the air date.
     *
     * @param episode the episode
     * @param date the date
     */
    private void setAirDate(EpisodeBean episode, String date) {
        String unaired = "UNAIRED";
        unaired = "\"" + unaired + "\"";
        if (!unaired.equals(date) && !"UNKNOWN".equals(date) && !"UNAIRED".equals(date)) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Episode.DATE_PATTERN, Locale.US);
                date = date.replaceAll("\"", "");
                Date airDate = simpleDateFormat.parse(date);
                episode.setAirDate(airDate);
            } catch (ParseException e) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Episode.DATE_OPTIONAL_PATTERN, Locale.US);
                Date airDate;
                try {
                    airDate = simpleDateFormat.parse(date);
                    episode.setAirDate(airDate);
                } catch (ParseException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bitmap createBitmap(SeriesBean series) {
        if (series == null || series.getDirectory() == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }
        String url = EPGUIDES_URL + series.getDirectory() + "/cast.jpg";
        InputStream inputStream = createInputStream(url);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, 125, 100, true);
        }
        return bitmap;
    }

    /**
     * Gets the context.
     *
     * @return the context
     */
    public Context getContext() {
        return context;
    }

    /**
     * Sets the context.
     *
     * @param context the context to set
     */
    public void setContext(Context context) {
        this.context = context;
    }
    
}
