package be.asers;

import android.app.Application;
import be.asers.service.FinderService;
import be.asers.service.impl.FinderServiceImpl;

/**
 * Asers application.
 * 
 * @author chesteric31
 */
public class AsersApplication extends Application {

    private FinderService finderService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();
        finderService = new FinderServiceImpl(this);
    }

    /**
     * @return the finderService
     */
    public FinderService getFinderService() {
        return finderService;
    }

}
