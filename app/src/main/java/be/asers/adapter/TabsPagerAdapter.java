package be.asers.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import be.asers.fragment.MySeriesFragment;
import be.asers.fragment.SeriesManagementFragment;

/**
 * {@link FragmentPagerAdapter} with tabs.
 * 
 * @author chesteric31
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    /**
     * Constructor.
     * 
     * @param fm the {@link FragmentManager}
     */
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fragment getItem(int index) {
        Fragment fragment = null;
        switch (index) {
        case 0:
            fragment = MySeriesFragment.getInstance();
            break;
        case 1:
            fragment = SeriesManagementFragment.getInstance(this);
            break;
        default:
            break;
        }
        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return 2;
    }
    
    /**
     * Refresh my series.
     */
    public void refreshMySeries() {
        MySeriesFragment.getInstance().refresh();
    }

}
