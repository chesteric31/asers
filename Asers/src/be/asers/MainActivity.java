package be.asers;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import be.asers.bean.SeriesBean;
import be.asers.dao.SeriesDao;
import be.asers.service.Finder;

/**
 * First activity of the project.
 *
 * @author chesteric31
 */
public class MainActivity extends Activity {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner mySeriesSpinner = (Spinner) findViewById(R.id.my_series_spinner);
        Finder finder = new Finder(this);
        finder.setSeriesDao(new SeriesDao(this));
        List<SeriesBean> mySeries = finder.findMySeries();
        ArrayAdapter<SeriesBean> dataAdapter = new ArrayAdapter<SeriesBean>(this,
            android.R.layout.simple_spinner_item, mySeries);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySeriesSpinner.setAdapter(dataAdapter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
