package be.asers;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import be.asers.bean.SeriesBean;

/**
 * Activity to add a new series.
 * 
 * @author chesteric31
 */
public class AddSeriesActivity extends Activity implements TextWatcher {

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
        addSeriesAutoComplete.addTextChangedListener(this);
        arrayAdapter = new ArrayAdapter<SeriesBean>(this, android.R.layout.simple_dropdown_item_1line);
        arrayAdapter.setNotifyOnChange(true);
        addSeriesAutoComplete.setAdapter(arrayAdapter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_series, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterTextChanged(Editable arg0) {
        // TODO Auto-generated method stub
//        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
//        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTextChanged(CharSequence string, int arg1, int arg2, int arg3) {
        arrayAdapter.clear();
        String currentText = addSeriesAutoComplete.getText().toString();
        if (addSeriesAutoComplete.getThreshold() <= currentText.length()) {
            AsersApplication application = (AsersApplication) getApplication();
            SeriesBean series = application.getFinderService().findSeries(currentText);
            if (series != null) {
                Toast.makeText(this, "We have found: " + series, Toast.LENGTH_LONG).show();
            }
        }
    }

}
