package be.asers;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import be.asers.bean.SeriesBean;
import be.asers.service.Finder;

/**
 * First activity of the project.
 *
 * @author chesteric31
 */
public class MainActivity extends Activity {

    protected static final int ADD_SERIES_REQUEST = 0;
    private Finder finder = new Finder(this);

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fillSpinnerData();
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
            // On v�rifie aussi que l'op�ration s'est bien d�roul�e
            if (resultCode == RESULT_OK) {
                // On affiche le bouton qui a �t� choisi
                Toast.makeText(this, "blbla", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Fills the spinner with my series with the data found in database.
     */
    private void fillSpinnerData() {
        Spinner mySeriesSpinner = (Spinner) findViewById(R.id.my_series_spinner);
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
