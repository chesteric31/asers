package be.asers.service;

import java.util.List;

import be.asers.bean.EpisodeBean;
import be.asers.bean.ShowBean;
import be.asers.dao.ShowDao;

/**
 * Finder service interface.
 * 
 * @author chesteric31
 */
public interface FinderService {

    /**
     * Finds the series where the status has active.
     * 
     * @return the found {@link ShowBean}s with active status
     */
    List<ShowBean> findMyShows();

    /**
     * Finds the series following the title criteria.
     * 
     * @param title the title to use
     * @return the found {@link ShowBean} entity
     */
    ShowBean findShow(String title);

    ShowBean addShow(ShowBean series);

    void addMyShow(ShowBean show);

    /**
     * Gets the series dao.
     *
     * @return the seriesDao
     */
    ShowDao getShowDao();

    /**
     * Deletes a {@link ShowBean} from our favorites, i.e.: set to INACTIVE.
     * 
     * @param show the {@link ShowBean} to set to INACTIVE
     */
    void deleteMyShow(ShowBean show);

    /**
     * Finds air date of episode after today.
     * 
     * @param show the {@link ShowBean} to use
     * @return the found air date of episode after today
     */
    EpisodeBean findAirDateNextEpisode(ShowBean show);

    /**
     * Refresh series.
     *
     * @param show the show to refresh
     */
    void refreshShow(ShowBean show);

}
