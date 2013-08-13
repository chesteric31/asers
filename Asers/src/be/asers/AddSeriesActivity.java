package be.asers;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import be.asers.bean.SeriesBean;

/**
 * Activity to add a new series.
 * 
 * @author chesteric31
 */
public class AddSeriesActivity extends Activity {

    private ArrayAdapter<SeriesBean> arrayAdapter;
    private AutoCompleteTextView addSeriesAutoComplete;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_series);
        addSeriesAutoComplete = (AutoCompleteTextView) findViewById(R.id.add_series_auto_complete);
        AsersApplication application = (AsersApplication) getApplication();
        List<SeriesBean> series = application.getFinderService().findAllSeries();
        arrayAdapter = new ArrayAdapter<SeriesBean>(this, android.R.layout.simple_dropdown_item_1line, series);
        addSeriesAutoComplete.setAdapter(arrayAdapter);
    }

}
