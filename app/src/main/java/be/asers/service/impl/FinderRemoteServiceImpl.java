package be.asers.service.impl;

import android.content.Context;
import android.graphics.Bitmap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.asers.bean.QueryShowBean;
import be.asers.bean.ShowBean;
import be.asers.model.Show;
import be.asers.service.FinderRemoteService;

/**
 * {@link Show} Finder remote (from epguides.com) service.
 *
 * @author chesteric31
 */
public class FinderRemoteServiceImpl implements FinderRemoteService {

    /**
     * The Constant CSV_DELIMITER.
     */
    private static final String CSV_DELIMITER = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    /**
     * The Constant MIN.
     */
    private static final String MIN = "min";

    /**
     * The Constant EPS.
     */
    private static final String EPS = "eps";

    /**
     * The Constant DOUBLE_QUOTES.
     */
    private static final String DOUBLE_QUOTES = "\"";

    /**
     * The Constant EMPTY_STRING.
     */
    private static final String EMPTY_STRING = "";

    /**
     * The Constant END_DATA_DELIMITER.
     */
    private static final String END_DATA_DELIMITER = "</pre>";

    /**
     * The Constant HEADER_NUMBER_LINES.
     */
    private static final int HEADER_NUMBER_LINES = 12;

    /**
     * The Constant FIRST_COLUMN_TITLE.
     */
    private static final String FIRST_COLUMN_TITLE = "number";

    /**
     * The Constant NOT_SPECIAL_EPISODE.
     */
    private static final String NOT_SPECIAL_EPISODE = "n";
    private static final String HTTP_API_TVMAZE_COM_SEARCH_SHOWS_QUERY = "http://api.tvmaze.com/search/shows?q=";
    private static final String HTTP_API_TVMAZE_COM_SINGLESEARCH_SHOWS_QUERY = "http://api.tvmaze.com/singlesearch/shows?q=";

    /**
     * The context.
     */
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
    public ShowBean findShow(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The series title cannot be empty!");
        }
        return findByName(name);
    }

    private ShowBean findByName(String name) {
        if (name != null) {
            final String url = HTTP_API_TVMAZE_COM_SINGLESEARCH_SHOWS_QUERY + name;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<ShowBean> response = restTemplate.getForEntity(url, ShowBean.class);
            return response.getBody();
        }
        return null;
    }

    @Override
    public List<ShowBean> findShowsByKeywords(String keywords) {
        List<ShowBean> shows = new ArrayList<ShowBean>();
        if (keywords != null) {
            final String url = HTTP_API_TVMAZE_COM_SEARCH_SHOWS_QUERY + keywords;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<QueryShowBean[]> response = restTemplate.getForEntity(url, QueryShowBean[].class);
            shows = mapQueriesToShows(response.getBody());
        }
        return shows;
    }

    public static void main(String... args) {
        try {
            URL url = new URL("http://api.tvmaze.com/shows/" + 4 + "?embed=nextepisode");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            System.out.println("Response Code: " + conn.getResponseCode());
            InputStream in = new BufferedInputStream(conn.getInputStream());

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(responseStrBuilder.toString());
            String airStamp = actualObj.path("_embedded").path("nextepisode").path("airstamp").toString().replace("\"", "");
            System.out.println(airStamp.substring(0, 19));
            java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(airStamp.replace("T", " ").substring(0, 19));
            Date date = new Date(timestamp.getTime());
            System.out.println(date.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Date findAirDateNextEpisode(ShowBean show) {
        if (show != null) {
            try {
                URL url = new URL("http://api.tvmaze.com/shows/" + show.getTvMazeId() + "?embed=nextepisode");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = streamReader.readLine()) != null)
                    stringBuilder.append(line);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(stringBuilder.toString());
                String airStamp = jsonNode.path("_embedded").path("nextepisode").path("airstamp").toString();
                airStamp = airStamp.replace("\"", "");
                airStamp = airStamp.replace("T", " ");
                airStamp = airStamp.substring(0, 19);
                java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(airStamp);
                return new Date(timestamp.getTime());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private List<ShowBean> mapQueriesToShows(QueryShowBean[] queries) {
        List<ShowBean> shows = new ArrayList<ShowBean>();
        for (QueryShowBean query : queries) {
            shows.add(query.getShow());
        }
        return shows;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bitmap createBitmap(ShowBean show) {
        if (show == null || show.getImage() == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }
        /**InputStream inputStream = new
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, 125, 100, true);
        }*/
        return null;
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
