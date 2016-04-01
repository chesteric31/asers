package be.asers.service;

import android.graphics.Bitmap;

import java.util.List;

import be.asers.bean.SeriesBean;
import be.asers.bean.ShowBean;

/**
 * Finder service interface.
 * 
 * @author chesteric31
 */
public interface FinderRemoteService {

    /**
     * Finds the series following the title criteria.
     * 
     * @param name the title to use
     * @return the found {@link SeriesBean} entity
     */
    ShowBean findShow(String name);

    /**
     * Creates the bitmap cast image from {@link ShowBean}.
     * 
     * @param show the {@link ShowBean} to use
     * @return the created {@link Bitmap}
     */
    Bitmap createBitmap(ShowBean show);

    List<ShowBean> findShowsByKeywords(String keywords);
}
