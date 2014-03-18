package be.asers.service;

import java.util.List;

import be.asers.bean.EpisodeBean;
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
     * @return the added SeriesBean 
     */
    SeriesBean addSeries(SeriesBean series);

    /**
     * Creates a new {@link SeriesBean} from a {@link SeriesBean} in my
     * favorites.
     * 
     * @param series the {@link SeriesBean} to add as favorite
     */
    void addMySeries(SeriesBean series);

    /**
     * @return the seriesDao
     */
    SeriesDao getSeriesDao();

    /**
     * Deletes a {@link SeriesBean} from our favorites, i.e.: set to INACTIVE.
     * 
     * @param series the {@link SeriesBean} to set to INACTIVE
     */
    void deleteMySeries(SeriesBean series);

    /**
     * Finds air date of episode after today.
     * 
     * @param series the {@link SeriesBean} to use
     * @return the found air date of episode after today
     */
    EpisodeBean findAirDateNextEpisode(SeriesBean series);

}
