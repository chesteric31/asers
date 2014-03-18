package be.asers.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import be.asers.AsersApplication;
import be.asers.AsersUncaughtExceptionHandler;
import be.asers.R;
import be.asers.bean.SeriesBean;
import be.asers.service.FinderService;

/**
 * Activity to delete new series i.e.: set to INACTIVE
 * 
 * @author chesteric31
 */
public class DeleteSeriesActivity extends Activity {

    private AutoCompleteTextView deleteSeriesAutoComplete;
    private ArrayAdapter<SeriesBean> arrayAdapter;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new AsersUncaughtExceptionHandler(this));
        setContentView(R.layout.activity_delete_series);
        deleteSeriesAutoComplete = (AutoCompleteTextView) findViewById(R.id.delete_series_auto_complete);
        buildArrayAdapter();
        deleteSeriesAutoComplete.setAdapter(arrayAdapter);
        deleteSeriesAutoComplete.setOnItemClickListener(buildOnItemClickListener());
    }

    /**
     * Builds {@link OnItemClickListener}.
     * 
     * @return the built {@link OnItemClickListener}
     */
    private OnItemClickListener buildOnItemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SeriesBean selected = (SeriesBean) parent.getAdapter().getItem(position);
                AlertDialog alert = buildConfirmationDialog(DeleteSeriesActivity.this,
                        R.string.delete_series_confirmation);
                alert.show();
                Toast.makeText(DeleteSeriesActivity.this,
                        DeleteSeriesActivity.this.getString(R.string.add_series_selected) + selected,
                        Toast.LENGTH_SHORT).show();
                try {
                    new DeleteSeriesTask().execute(selected).get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e.getCause());
                }
            }

            private AlertDialog buildConfirmationDialog(Context context, int messageId) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(messageId);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.delete_series_confirmation_yes,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                builder.setNegativeButton(R.string.delete_series_confirmation_no,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                return alert;
            }
        };
    }

    /**
     * Builds {@link ArrayAdapter}.
     */
    private void buildArrayAdapter() {
        final List<SeriesBean> series = new ArrayList<SeriesBean>();
        new FindActiveSeriesTask(new OnCompleteTaskListener<List<SeriesBean>>() {

            @Override
            public void onComplete(List<SeriesBean> result) {
                series.addAll(result);
            }
        }).execute();
        arrayAdapter = new ArrayAdapter<SeriesBean>(this, android.R.layout.simple_dropdown_item_1line, series);
    }

    /**
     * Asynchronous task to set INACTIVE a {@link SeriesBean}.
     * 
     * @author chesteric31
     */
    private class DeleteSeriesTask extends AsyncTask<SeriesBean, Void, Void> {

        /**
         * {@inheritDoc}
         */
        @Override
        protected Void doInBackground(SeriesBean... params) {
            if (params == null || params.length == 0) {
                throw new IllegalArgumentException("A series bean must be given!");
            }
            AsersApplication asersApplication = (AsersApplication) getApplication();
            asersApplication.getFinderService().deleteMySeries(params[0]);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delete_series, menu);
        return true;
    }

    /**
     * Asynchronous task to find all ACTIVE {@link SeriesBean}s.
     * 
     * @author chesteric31
     */
    private class FindActiveSeriesTask extends AbstractOnCompleteAsyncTask<Void, Void, List<SeriesBean>> {

        /**
         * Constructor.
         * 
         * @param onCompleteTaskListener the {@link OnCompleteTaskListener} to
         *            use
         */
        public FindActiveSeriesTask(OnCompleteTaskListener<List<SeriesBean>> onCompleteTaskListener) {
            super(onCompleteTaskListener);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<SeriesBean> doInBackground(Void... params) {
            FinderService finderService = ((AsersApplication) getApplication()).getFinderService();
            return finderService.findMySeries();
        }
    }

}
