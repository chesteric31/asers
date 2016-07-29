package be.asers.service;

import java.util.List;

import be.asers.bean.ShowBean;
import be.asers.dao.ShowDao;

/**
 * Finder service interface.
 * 
 * @author chesteric31
 */
public interface FinderService {

    List<ShowBean> findMyShows();

    ShowBean findShow(String title);

    ShowBean addShow(ShowBean series);

    void addMyShow(ShowBean show);

    ShowDao getShowDao();

    void deleteMyShow(ShowBean show);

}
