package be.asers.service;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.List;

import be.asers.bean.ShowBean;

/**
 * Finder service interface.
 * 
 * @author chesteric31
 */
public interface FinderRemoteService {

    ShowBean findShow(String name);

    Bitmap createBitmap(ShowBean show);

    List<ShowBean> findShowsByKeywords(String keywords);

    Date findAirDateNextEpisode(ShowBean show);

}
