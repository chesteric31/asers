package be.asers;

import java.io.BufferedReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import be.asers.bean.SeriesBean;
import be.asers.service.FinderService;

/**
 * Activity to add a new series.
 * 
 * @author chesteric31
 */
public class AddSeriesActivity extends Activity {

    private static final String CSV_DELIMITER = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
    private ArrayAdapter<SeriesBean> arrayAdapter;
    private AutoCompleteTextView addSeriesAutoComplete;
    private ProgressDialog progressDialog;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new AsersUncaughtExceptionHandler(this));

        setContentView(R.layout.activity_add_series);
        buildProgressDialog();
        addSeriesAutoComplete = (AutoCompleteTextView) findViewById(R.id.add_series_auto_complete);
        final List<SeriesBean> series = new ArrayList<SeriesBean>();
        new FindAllSeriesTask(AddSeriesActivity.this, new OnCompleteTaskListener() {

            @Override
            public void onComplete(List<SeriesBean> result) {
                series.addAll(result);
            }
        }).execute();
        arrayAdapter = new ArrayAdapter<SeriesBean>(this, android.R.layout.simple_dropdown_item_1line, series);
        addSeriesAutoComplete.setAdapter(arrayAdapter);
        addSeriesAutoComplete.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SeriesBean selected = (SeriesBean) parent.getAdapter().getItem(position);
                Toast.makeText(AddSeriesActivity.this,
                        AddSeriesActivity.this.getString(R.string.add_series_selected) + selected, Toast.LENGTH_SHORT)
                        .show();
                try {
                    new AddSeriesTask().execute(selected).get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Updates the {@link ProgressDialog} with the progress parameter.
     * 
     * @param progress the progress to use
     */
    private void updateProgress(int progress) {
        getProgressDialog().setProgress(progress);
    }

    /**
     * Builds the {@link ProgressDialog}.
     */
    private void buildProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(this.getString(R.string.add_series_loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    /**
     * @return the progressDialog
     */
    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    /**
     * Asynchronous task to add {@link SeriesBean} to favorites
     * {@link SeriesBean}.
     * 
     * @author chesteric31
     */
    private class AddSeriesTask extends AsyncTask<SeriesBean, Void, Void> {

        /**
         * {@inheritDoc}
         */
        @Override
        protected Void doInBackground(SeriesBean... params) {
            if (params == null || params.length == 0) {
                throw new IllegalArgumentException("A series bean must be given!");
            }
            AsersApplication asersApplication = (AsersApplication) getApplication();
            asersApplication.getFinderService().addMySeries(params[0]);
            return null;
        }
    }

    /**
     * Interface to signal that the task is completed.
     * 
     * @author chesteric31
     */
    private interface OnCompleteTaskListener {

        /**
         * Method called on complete.
         * 
         * @param result the result: list of {@link SeriesBean}
         */
        void onComplete(List<SeriesBean> result);
    }

    /**
     * Asynchronous task to find all available {@link SeriesBean}s.
     * 
     * @author chesteric31
     */
    private class FindAllSeriesTask extends AsyncTask<Void, Integer, List<SeriesBean>> {

        private WeakReference<AddSeriesActivity> activity = null;
        private final OnCompleteTaskListener onCompleteTaskListener;

        /**
         * Constructor.
         * 
         * @param addSeriesActivity the {@link AddSeriesActivity} to use
         * @param taskListener the {@link OnCompleteTaskListener} to use
         */
        public FindAllSeriesTask(AddSeriesActivity addSeriesActivity, OnCompleteTaskListener taskListener) {
            activity = new WeakReference<AddSeriesActivity>(addSeriesActivity);
            onCompleteTaskListener = taskListener;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPreExecute() {
            AddSeriesActivity addSeriesActivity = activity.get();
            if (addSeriesActivity != null) {
                ProgressDialog dialog = addSeriesActivity.getProgressDialog();
                dialog.show();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPostExecute(List<SeriesBean> result) {
            AddSeriesActivity addSeriesActivity = activity.get();
            if (addSeriesActivity != null) {
                Toast.makeText(addSeriesActivity, AddSeriesActivity.this.getString(R.string.add_series_loading_done),
                        Toast.LENGTH_SHORT).show();
                ProgressDialog dialog = addSeriesActivity.getProgressDialog();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                onCompleteTaskListener.onComplete(result);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<SeriesBean> doInBackground(Void... params) {
            List<SeriesBean> series = new ArrayList<SeriesBean>();
            FinderService finderService = ((AsersApplication) getApplication()).getFinderService();
            BufferedReader bufferedReader = finderService.createReader();
            List<String> contents = finderService.createStringsContent(bufferedReader);
            AddSeriesActivity addSeriesActivity = activity.get();
            if (addSeriesActivity != null) {
                addSeriesActivity.getProgressDialog().setMax(contents.size());
            }
            int total = 0;
            Log.d(AddSeriesActivity.class.getSimpleName(), "START LOOP");
            for (String content : contents) {
                if (total == 50) {
                    break;
                }
                if (content.length() > 0) {
                    total++;
                    String[] tokens = new String[9];
                    String[] splits = content.split(CSV_DELIMITER);
                    int i = 0;
                    for (String split : splits) {
                        tokens[i] = split;
                        i++;
                    }
                    SeriesBean bean = finderService.buildSeries(tokens);
                    series.add(bean);
                    publishProgress(total);
                }
            }
            Log.d(AddSeriesActivity.class.getSimpleName(), "END LOOP");
            return series;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            AddSeriesActivity addSeriesActivity = activity.get();
            if (addSeriesActivity != null) {
                addSeriesActivity.updateProgress(values[0]);
            }
        }
        
    }

}
