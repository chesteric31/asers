package be.asers.service.impl;

import android.content.Context;
import android.graphics.Bitmap;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

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

    @Override
    public Date findAirDateNextEpisode(ShowBean show) {
        if (show != null) {
            String url = "http://api.tvmaze.com/shows/" + show.getTvMazeId() + "?embed=nextepisode";
//            MappingJackson2HttpMessageConverter converter= new MappingJackson2HttpMessageConverter();
//            converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//            mapper.registerModule(new Jackson2HalModule());
//            converter.setObjectMapper(mapper);
//            RestTemplate restTemplate = new RestTemplate(Collections.<HttpMessageConverter<?>> singletonList(converter));
//            restTemplate.getMessageConverters().add(converter);
//            System.out.println(restTemplate.getForObject(url, PagedResources.class).getContent().size());
//            System.out.println(restTemplate.getForObject(url, PagedResources.class).getLinks().size());
//            System.out.println(restTemplate.getForObject(url, PagedResources.class).getMetadata().getTotalElements());

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
