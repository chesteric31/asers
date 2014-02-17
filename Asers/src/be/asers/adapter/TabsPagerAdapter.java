package be.asers.adapter;

import be.asers.fragment.MySeriesFragment;
import be.asers.fragment.NextEpisodesFragment;
import be.asers.fragment.SeriesManagementFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
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
            fragment = new MySeriesFragment();
            break;
        case 1:
            fragment = new NextEpisodesFragment();
            break;
        case 2:
            fragment = new SeriesManagementFragment();
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
        return 3;
    }

}
