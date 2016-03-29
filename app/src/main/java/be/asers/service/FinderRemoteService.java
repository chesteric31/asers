package be.asers.service;

import java.io.BufferedReader;
import java.util.List;

import android.graphics.Bitmap;
import be.asers.bean.SeriesBean;

/**
 * Finder service interface.
 * 
 * @author chesteric31
 */
public interface FinderRemoteService {

    /**
     * Finds the series following the title criteria.
     * 
     * @param title the title to use
     * @return the found {@link SeriesBean} entity
     */
    SeriesBean findSeries(String title);

    /**
     * Creates a {@link BufferedReader} from the URL:
     * "http://epguides.com/common/allshows.txt".
     * 
     * @param url the URL to use, or if null:
     *            "http://epguides.com/common/allshows.txt"
     * @return the created {@link BufferedReader}
     */
    BufferedReader createReader(String url);
    
    /**
     * Creates the bitmap cast image from {@link SeriesBean}.
     * 
     * @param series the {@link SeriesBean} to use
     * @return the created {@link Bitmap}
     */
    Bitmap createBitmap(SeriesBean series);

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
     * Builds a "skinny" {@link SeriesBean} from the tokens only with title and
     * network.
     * 
     * @param tokens the tokens to use
     * @return the built skinny {@link SeriesBean}
     */
    SeriesBean buildSkinnySeries(String[] tokens);

}
