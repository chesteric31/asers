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
import be.asers.bean.SeriesBean;
import be.asers.dao.SeriesDao;
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
        List<String> contents;
        try {
            URL url = new URL(ALL_SERIES_URL);
            BufferedReader bufferedReader = new UrlReaderTask(this.context).execute(url).get();
            contents = new ReaderStringTask().execute(bufferedReader).get();
            for (String content : contents) {
                String[] tokens = content.split(",");
                for (String token : tokens) {
                    // if (token.matches("(.*)" + title + "(.*)")) {
                    if (token.equalsIgnoreCase(title)) {
                        SeriesBean bean = buildSeries(tokens);
                        return addSeries(bean);
                    }
                }
            }
        } catch (IOException e) {
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
     * Builds the tv rage id.
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
     * @return true if the all the character of the String is digit, false otherwise
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
     * {@inheritDoc}
     */
    @Override
    public SeriesBean findSeriesDetails(String title) throws IOException {
        SeriesBean series = findSeries(title);
        URL url = new URL(CSV_EXPORT_URL + series.getTvRageId());
        BufferedReader reader;
        try {
            reader = new UrlReaderTask(this.context).execute(url).get();
            new ReaderSeasonTask(series).execute(reader).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
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
    public BufferedReader createReader() {
        URLConnection connection;
        BufferedReader reader;
        try {
            URL url = new URL(ALL_SERIES_URL);
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

}
