package be.asers.activity;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import be.asers.AsersApplication;
import be.asers.R;
import be.asers.bean.SeriesBean;

/**
 * Main activity of the project.
 * 
 * @author chesteric31
 */
class MainActivity extends Activity {

    private static final int ADD_SERIES_REQUEST = 0;
    private TableLayout mySeriesTable;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySeriesTable = (TableLayout) findViewById(R.id.my_series_table);
        fillTableData(mySeriesTable);
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
                fillTableData(mySeriesTable);
            }
        }
    }

    /**
     * Fills the spinner with my series with the data found in database.
     * 
     * @param mySeriesTable the spinner to use
     */
    private void fillTableData(TableLayout mySeriesTable) {
        List<SeriesBean> mySeries;
        try {
            mySeries = new MySeriesFinderTask().execute().get();
            TextView textView = null;
            TableRow tableRow = null;
            for (SeriesBean series : mySeries) {
                textView = new TextView(this);
                textView.setText(series.toString());
                tableRow = new TableRow(this);
                tableRow.addView(textView);
                mySeriesTable.addView(tableRow);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
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

    /**
     * Asynchronous task to find my {@link SeriesBean}.
     * 
     * @author chesteric31
     */
    private class MySeriesFinderTask extends AsyncTask<Void, Void, List<SeriesBean>> {

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<SeriesBean> doInBackground(Void... params) {
            AsersApplication asersApplication = (AsersApplication) getApplication();
            return asersApplication.getFinderService().findMySeries();
        }
    }

}
