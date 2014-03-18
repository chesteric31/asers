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

    private FinderService finderService;
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
     * @return the finderService
     */
    public FinderService getFinderService() {
        return finderService;
    }

    /**
     * @return the finderRemoteService
     */
    public FinderRemoteService getFinderRemoteService() {
        return finderRemoteService;
    }
    
}
