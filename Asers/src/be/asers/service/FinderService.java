package be.asers.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import be.asers.bean.SeriesBean;
import be.asers.dao.SeriesDao;

/**
 * Finder service interface.
 * 
 * @author chesteric31
 */
public interface FinderService {

    /**
     * Finds the series where the status has active.
     * 
     * @return the found {@link SeriesBean}s with active status
     */
    List<SeriesBean> findMySeries();

    /**
     * Finds the series following the title criteria.
     * 
     * @param title the title to use
     * @return the found {@link SeriesBean} entity
     */
    SeriesBean findSeries(String title);

    /**
     * Creates a new {@link SeriesBean} from a {@link SeriesBean}.
     * 
     * @param series the {@link SeriesBean} to use
     * @return the created new {@link SeriesBean}
     */
    SeriesBean addSeries(SeriesBean series);

    /**
     * Creates a new {@link SeriesBean} from a {@link SeriesBean} in my
     * favorites.
     * 
     * @param series the {@link SeriesBean} to add as favorite
     * @return the created new {@link SeriesBean}
     */
    SeriesBean addMySeries(SeriesBean series);

    /**
     * Finds the {@link SeriesBean} details following the title criteria.
     * 
     * @param title the title to use
     * @return the found {@link SeriesBean}
     * @throws IOException if an error occurred
     */
    SeriesBean findSeriesDetails(String title) throws IOException;

    /**
     * @return the seriesDao
     */
    SeriesDao getSeriesDao();

    /**
     * Creates a {@link BufferedReader} from the URL:
     * "http://epguides.com/common/allshows.txt".
     * 
     * @param url the URL to use, or if null: "http://epguides.com/common/allshows.txt" 
     * @return the created {@link BufferedReader}
     */
    BufferedReader createReader(URL url);

    /**
     * Creates a list of Strings from an {@link BufferedReader}.
     * 
     * @param bufferedReader the {@link BufferedReader} to use
     * @return the created list of Strings
     */
    List<String> createStringsContent(BufferedReader bufferedReader);

    /**
     * Builds a {@link SeriesBean} from the tokens.
     * 
     * @param tokens the tokens to use
     * @return the built {@link SeriesBean}
     */
    SeriesBean buildSeries(String[] tokens);

    /**
     * Builds a "skinny" {@link SeriesBean} from the tokens only with title.
     * 
     * @param tokens the tokens to use
     * @return the built skinny {@link SeriesBean}
     */
    SeriesBean buildSkinnySeries(String[] tokens);

}
