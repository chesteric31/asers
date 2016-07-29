package be.asers.service.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private static final String HTTP_API_TVMAZE_COM_SEARCH_SHOWS_QUERY = "http://api.tvmaze.com/search/shows?q=";
    private static final String HTTP_API_TVMAZE_COM_SINGLESEARCH_SHOWS_QUERY = "http://api.tvmaze.com/singlesearch/shows?q=";
    private static final String AIR_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
    private Context context;

    public FinderRemoteServiceImpl(Context context) {
        this.context = context;
    }

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

    @Override
    public Date findAirDateNextEpisode(ShowBean show) {
        if (show != null) {
            try {
                URL url = new URL("http://api.tvmaze.com/shows/" + show.getTvMazeId() + "?embed=nextepisode");
                StringBuilder stringBuilder = buildStringBuilder(url);
                JsonNode jsonNode = buildJsonNode(stringBuilder);
                String airStamp = retrieveAirStamp(jsonNode).toString();
                if (!airStamp.isEmpty()) {
                    return parseAirStampIntoDate(airStamp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private JsonNode retrieveAirStamp(JsonNode jsonNode) {
        return retrieveNextEpisode(jsonNode).path("airstamp");
    }

    private JsonNode retrieveNextEpisode(JsonNode jsonNode) {
        return jsonNode.path("_embedded").path("nextepisode");
    }

    private JsonNode buildJsonNode(StringBuilder stringBuilder) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(stringBuilder.toString());
    }

    private StringBuilder buildStringBuilder(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = new BufferedInputStream(connection.getInputStream());
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = streamReader.readLine()) != null)
            stringBuilder.append(line);
        return stringBuilder;
    }

    private Date parseAirStampIntoDate(String airStamp) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(AIR_DATE_PATTERN);
        String airStampWithoutQuotes = airStamp.replace("\"", "");
        StringBuilder builder = new StringBuilder(airStampWithoutQuotes);
        int lastIndexOf = airStamp.lastIndexOf(":");
        builder = builder.replace(lastIndexOf, lastIndexOf + 1, "");
        String formattedAirStamp = builder.toString();
        try {
            return format.parse(formattedAirStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<ShowBean> mapQueriesToShows(QueryShowBean[] queries) {
        List<ShowBean> shows = new ArrayList<ShowBean>();
        for (QueryShowBean query : queries) {
            ShowBean show = query.getShow();
            shows.add(show);
        }
        return shows;
    }

    @Override
    public Bitmap createBitmap(ShowBean show) {
        if (show == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }
        if (show.getImage() != null) {
            String url = show.getImage().getMedium();
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                if (bitmap != null) {
                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
