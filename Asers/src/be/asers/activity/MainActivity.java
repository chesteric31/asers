package be.asers.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.TabHost.TabSpec;
import be.asers.R;
import be.asers.fragment.MySeriesFragment;
import be.asers.fragment.NextEpisodesFragment;
import be.asers.fragment.SeriesManagementFragment;

/**
 * Main activity of the project.
 * 
 * @author chesteric31
 */
public class MainActivity extends FragmentActivity {

    private FragmentTabHost tabHost;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        CharSequence mySeriesTitle = getResources().getText(R.string.my_series_label);
        TabSpec mySeriesTabSpec = tabHost.newTabSpec("my_series");
        tabHost.addTab(mySeriesTabSpec.setIndicator(mySeriesTitle), MySeriesFragment.class, null);
        CharSequence nextEpisodesTitle = getResources().getText(R.string.next_episodes_label);
        TabSpec nextEpisodesTabSpec = tabHost.newTabSpec("next_episodes");
        tabHost.addTab(nextEpisodesTabSpec.setIndicator(nextEpisodesTitle), NextEpisodesFragment.class, null);
        CharSequence seriesManagementTitle = getResources().getText(R.string.series_management_label);
        TabSpec seriesManagementTabSpec = tabHost.newTabSpec("series_management");
        tabHost.addTab(seriesManagementTabSpec.setIndicator(seriesManagementTitle), SeriesManagementFragment.class,
                null);
    }

}
