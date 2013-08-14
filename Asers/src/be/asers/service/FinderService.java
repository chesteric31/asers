package be.asers.service;

import java.io.IOException;
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
     * Creates a new {@link SeriesBean} from a {@link SeriesBean} in my favorites.
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
     * Finds all available series.
     * 
     * @return all available {@link SeriesBean}
     */
    List<SeriesBean> findAllSeries();

}
