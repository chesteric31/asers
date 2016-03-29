package be.asers;

import android.app.Application;
import be.asers.service.FinderRemoteService;
import be.asers.service.FinderService;
import be.asers.service.impl.FinderRemoteServiceImpl;
import be.asers.service.impl.FinderServiceImpl;

/**
 * Asers application.
 * 
 * @author chesteric31
 */
public class AsersApplication extends Application {

    /** The finder service. */
    private FinderService finderService;
    
    /** The finder remote service. */
    private FinderRemoteService finderRemoteService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();
        finderService = new FinderServiceImpl(this);
        finderRemoteService = new FinderRemoteServiceImpl(this);
    }

    /**
     * Gets the finder service.
     *
     * @return the finderService
     */
    public FinderService getFinderService() {
        return finderService;
    }

    /**
     * Gets the finder remote service.
     *
     * @return the finderRemoteService
     */
    public FinderRemoteService getFinderRemoteService() {
        return finderRemoteService;
    }
    
}
