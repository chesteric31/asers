package be.asers.fragment;

import java.io.BufferedReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import be.asers.AsersApplication;
import be.asers.R;
import be.asers.activity.AbstractOnCompleteAsyncTask;
import be.asers.activity.OnCompleteTaskListener;
import be.asers.adapter.TabsPagerAdapter;
import be.asers.bean.SeriesBean;
import be.asers.service.FinderRemoteService;

/**
 * Fragment for Series management.
 * 
 * @author chesteric31
 */
public class SeriesManagementFragment extends Fragment {

    private static volatile SeriesManagementFragment instance = null;
    private static final String CSV_DELIMITER = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
    private ArrayAdapter<SeriesBean> arrayAdapter;
    private AutoCompleteTextView addSeriesAutoComplete;
    private ProgressDialog progressDialog;
    private TabsPagerAdapter tabsPagerAdapter;

    /**
     * Constructor.
     */
    public SeriesManagementFragment() {
        // Required empty public constructor
    }

    /**
     * @param tabsPagerAdapter 
     * @param tabsPagerAdapter 
     * @return singleton instance of {@link SeriesManagementFragment}
     */
    public static final SeriesManagementFragment getInstance(TabsPagerAdapter tabsPagerAdapter) {
        synchronized (SeriesManagementFragment.class) {
            if (SeriesManagementFragment.instance == null) {
                SeriesManagementFragment.instance = new SeriesManagementFragment();
                SeriesManagementFragment.instance.tabsPagerAdapter = tabsPagerAdapter;
            }
        }
        return SeriesManagementFragment.instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_series_management, container, false);
        buildProgressDialog();
        addSeriesAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.add_series_auto_complete);
        buildArrayAdapter();
        addSeriesAutoComplete.setAdapter(arrayAdapter);
        addSeriesAutoComplete.setOnItemClickListener(buildOnItemClickListener());
        return view;
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
                Toast.makeText(SeriesManagementFragment.this.getActivity(),
                        SeriesManagementFragment.this.getString(R.string.add_series_selected) + selected, Toast.LENGTH_SHORT)
                        .show();
                try {
                    new AddSeriesTask(SeriesManagementFragment.this).execute(selected).get();
                    tabsPagerAdapter.refreshMySeries();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e.getCause());
                }
            }
        };
    }

    /**
     * Builds {@link ArrayAdapter}.
     */
    private void buildArrayAdapter() {
        final List<SeriesBean> series = new ArrayList<SeriesBean>();
        new FindAllSeriesTask(SeriesManagementFragment.this, new OnCompleteTaskListener<List<SeriesBean>>() {

            @Override
            public void onComplete(List<SeriesBean> result) {
                series.addAll(result);
            }
        }).execute();
        arrayAdapter = new ArrayAdapter<SeriesBean>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, series);
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
        progressDialog = new ProgressDialog(getActivity());
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
        
        private WeakReference<SeriesManagementFragment> fragment = null;

        public AddSeriesTask(SeriesManagementFragment fragment) {
            this.fragment = new WeakReference<SeriesManagementFragment>(fragment);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Void doInBackground(SeriesBean... params) {
            if (params == null || params.length == 0) {
                throw new IllegalArgumentException("A series bean must be given!");
            }
            AsersApplication asersApplication = (AsersApplication) this.fragment.get().getActivity().getApplication();
            asersApplication.getFinderService().addMySeries(params[0]);
            return null;
        }
    }

    /**
     * Asynchronous task to find all available {@link SeriesBean}s.
     * 
     * @author chesteric31
     */
    private class FindAllSeriesTask extends AbstractOnCompleteAsyncTask<Void, Integer, List<SeriesBean>> {

        private WeakReference<SeriesManagementFragment> fragment = null;

        /**
         * Constructor.
         */
        public FindAllSeriesTask(SeriesManagementFragment fragment,
                OnCompleteTaskListener<List<SeriesBean>> taskListener) {
            super(taskListener);
            this.fragment = new WeakReference<SeriesManagementFragment>(fragment);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPreExecute() {
            SeriesManagementFragment addSeriesActivity = fragment.get();
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
            SeriesManagementFragment currentFragment = fragment.get();
            if (currentFragment != null) {
                Toast.makeText(currentFragment.getActivity(), SeriesManagementFragment.this.getString(R.string.add_series_loading_done),
                        Toast.LENGTH_SHORT).show();
                ProgressDialog dialog = currentFragment.getProgressDialog();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                super.onPostExecute(result);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<SeriesBean> doInBackground(Void... params) {
            FinderRemoteService finderService = ((AsersApplication) this.fragment.get().getActivity().getApplication()).getFinderRemoteService();
            BufferedReader bufferedReader = finderService.createReader(null);
            List<String> contents = finderService.createStringsContent(bufferedReader);
            SeriesManagementFragment currentFragment = fragment.get();
            int size = contents.size();
            if (currentFragment != null) {
                currentFragment.getProgressDialog().setMax(size);
            }
            long startTime = System.currentTimeMillis();
            List<SeriesBean> series = processSeries(finderService, contents);
            long seconds = (System.currentTimeMillis() - startTime) / 1000;
            String display = String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
            System.out.println("DURATION: " + display);
            return series;
        }

        /**
         * Processes the series.
         * 
         * @param finderService the {@link FinderRemoteService} to use
         * @param contents the contents to use
         * 
         * @return the processed {@link SeriesBean}s
         */
        private List<SeriesBean> processSeries(FinderRemoteService finderService, List<String> contents) {
            List<SeriesBean> list = new ArrayList<SeriesBean>();
            int total = 0;
            int size = contents.size();
            for (int i = 0; i < size; i++) {
                //TODO
//                if (total == 200) {
//                    break;
//                }
                String content = contents.get(i);
                if (content.length() > 0) {
                    total++;
                    String[] tokens = new String[9];
                    String[] splits = content.split(CSV_DELIMITER);
                    int length = splits.length;
                    for (int j = 0; j < length; j++) {
                        tokens[j] = splits[j];
                    }
                    SeriesBean bean = finderService.buildSkinnySeries(tokens);
                    list.add(bean);
                    publishProgress(total);
                }
            }
            return list;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            SeriesManagementFragment currentFragment = fragment.get();
            if (currentFragment != null) {
                currentFragment.updateProgress(values[0]);
            }
        }
    }

}
