package be.asers;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import be.asers.bean.SeriesBean;

/**
 * First activity of the project.
 * 
 * @author chesteric31
 */
public class MainActivity extends Activity {

    protected static final int ADD_SERIES_REQUEST = 0;
    private Spinner mySeriesSpinner;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySeriesSpinner = (Spinner) findViewById(R.id.my_series_spinner);
        mySeriesSpinner.setOnLongClickListener(new AdapterView.OnLongClickListener() {

            public boolean onLongClick(View view) {
                if (mySeriesSpinner.getSelectedItem() != null) {
                    Toast.makeText(MainActivity.this, mySeriesSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(MainActivity.this, "Nothing is selected", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        fillSpinnerData(mySeriesSpinner);
        addListenerOnAddSeriesButon();
    }

    /**
     * Adds listener on add series button.
     */
    private void addListenerOnAddSeriesButon() {
        Button addSeriesButton = (Button) findViewById(R.id.add_series_button);
        addSeriesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent addSeriesActivity = new Intent(MainActivity.this, AddSeriesActivity.class);
                startActivityForResult(addSeriesActivity, ADD_SERIES_REQUEST);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_SERIES_REQUEST) {
            if (resultCode == RESULT_CANCELED) {
                fillSpinnerData(mySeriesSpinner);
            }
        }
    }

    /**
     * Fills the spinner with my series with the data found in database.
     * 
     * @param mySeriesSpinner the spinner to use
     */
    private void fillSpinnerData(Spinner mySeriesSpinner) {
        AsersApplication application = (AsersApplication) getApplication();
        List<SeriesBean> mySeries = application.getFinderService().findMySeries();
        ArrayAdapter<SeriesBean> dataAdapter = new ArrayAdapter<SeriesBean>(this, android.R.layout.simple_spinner_item,
                mySeries);
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
