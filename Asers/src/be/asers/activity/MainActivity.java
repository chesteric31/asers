package be.asers.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
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
public class MainActivity extends Activity {

    private static final int REQUEST_CODE = 0;
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
                startActivityForResult(addSeriesActivity, REQUEST_CODE);
            }
        });
        Button deleteSeriesButton = (Button) findViewById(R.id.delete_series_button);
        deleteSeriesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent deleteSeriesActivity = new Intent(MainActivity.this, DeleteSeriesActivity.class);
                startActivityForResult(deleteSeriesActivity, REQUEST_CODE);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
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
    private void fillTableData(final TableLayout mySeriesTable) {
        final List<SeriesBean> mySeries = new ArrayList<SeriesBean>();
        new MySeriesFinderTask(new OnCompleteTaskListener<List<SeriesBean>>() {
            
            /**
             * {@inheritDoc}
             */
            @Override
            public void onComplete(List<SeriesBean> result) {
                mySeries.clear();
                clear(mySeriesTable);
                mySeries.addAll(result);
                TextView textView = null;
                TableRow tableRow = null;
                if (!mySeries.isEmpty()) {
                    for (SeriesBean series : mySeries) {
                        textView = new TextView(MainActivity.this);
                        textView.setTextColor(getResources().getColor(R.color.series_color));
                        textView.setText(series.toString());
                        tableRow = new TableRow(MainActivity.this);
                        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
                        tableRow.addView(textView);
                        mySeriesTable.addView(tableRow);
                    }
                } else {
                    clear(mySeriesTable);
                }
            }

            private void clear(final TableLayout mySeriesTable) {
                for (int i = 0; i < mySeriesTable.getChildCount(); i++) {
                    if (i > 0) {
                        // don't remove text view
                        TableRow row = (TableRow) mySeriesTable.getChildAt(i);
                        mySeriesTable.removeView(row);
                    }
                }
            }
        }).execute();
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
    private class MySeriesFinderTask extends AbstractOnCompleteAsyncTask<Void, Void, List<SeriesBean>> {

        /**
         * Constructor.
         * 
         * @param onCompleteTaskListener the {@link OnCompleteTaskListener} to
         *            use
         */
        public MySeriesFinderTask(OnCompleteTaskListener<List<SeriesBean>> onCompleteTaskListener) {
            super(onCompleteTaskListener);
        }

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
