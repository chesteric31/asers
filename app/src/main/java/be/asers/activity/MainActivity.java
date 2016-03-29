package be.asers.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import be.asers.AsersUncaughtExceptionHandler;
import be.asers.R;
import be.asers.adapter.TabsPagerAdapter;

/**
 * Main activity of the project.
 * 
 * @author chesteric31
 */
public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /** The view pager. */
    private ViewPager viewPager;
    
    /** The adapter. */
    private TabsPagerAdapter adapter;
    
    /** The action bar. */
    private ActionBar actionBar;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new AsersUncaughtExceptionHandler(this));
        
        setContentView(R.layout.activity_main);
        
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        adapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        CharSequence mySeriesTitle = getResources().getText(R.string.my_series_label);
        actionBar.addTab(actionBar.newTab().setText(mySeriesTitle).setTabListener(this));
        CharSequence seriesManagementTitle = getResources().getText(R.string.series_management_label);
        actionBar.addTab(actionBar.newTab().setText(seriesManagementTitle).setTabListener(this));
        setOnPageChangeListener();
    }

    /**
     * Sets {@link OnPageChangeListener} on {@link ViewPager}.
     */
    private void setOnPageChangeListener() {
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
            
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

}
