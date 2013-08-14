package be.asers;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
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
        final AsersApplication application = (AsersApplication) getApplication();
        List<SeriesBean> series = application.getFinderService().findAllSeries();
        arrayAdapter = new ArrayAdapter<SeriesBean>(this, android.R.layout.simple_dropdown_item_1line, series);
        addSeriesAutoComplete.setAdapter(arrayAdapter);
        addSeriesAutoComplete.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SeriesBean selected = (SeriesBean) parent.getAdapter().getItem(position);
                application.getFinderService().addMySeries(selected);
                Toast.makeText(AddSeriesActivity.this, "Added " + selected, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
