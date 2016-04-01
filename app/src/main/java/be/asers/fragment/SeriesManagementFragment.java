package be.asers.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

import be.asers.AsersApplication;
import be.asers.R;
import be.asers.adapter.ShowBeanListAdapter;
import be.asers.adapter.TabsPagerAdapter;
import be.asers.bean.ShowBean;

/**
 * Fragment for Series management.
 * 
 * @author chesteric31
 */
public class SeriesManagementFragment extends Fragment {

    /** The instance. */
    private static volatile SeriesManagementFragment instance = null;
    
    /** The array adapter. */
    private ShowBeanListAdapter adapter;

    /** The add series auto complete. */
    private AutoCompleteTextView addSeriesAutoComplete;
    
    /** The progress dialog. */
    private ProgressDialog progressDialog;
    
    /** The tabs pager adapter. */
    private TabsPagerAdapter tabsPagerAdapter;

    /**
     * Constructor.
     */
    public SeriesManagementFragment() {
        // Required empty public constructor
    }

    /**
     * Gets the single instance of SeriesManagementFragment.
     *
     * @param tabsPagerAdapter the tabs pager adapter
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
        addSeriesAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.add_series_auto_complete);
        adapter = new ShowBeanListAdapter(this.getActivity());
        addSeriesAutoComplete.setAdapter(adapter);
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
                ShowBean selected = (ShowBean) parent.getAdapter().getItem(position);
                Toast.makeText(SeriesManagementFragment.this.getActivity(),
                        SeriesManagementFragment.this.getString(R.string.add_series_selected) + selected, Toast.LENGTH_SHORT)
                        .show();
                try {
                    new AddShowTask(SeriesManagementFragment.this).execute(selected).get();
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
     * Asynchronous task to add {@link ShowBean} to favorites
     * {@link ShowBean}.
     * 
     * @author chesteric31
     */
    private class AddShowTask extends AsyncTask<ShowBean, Void, Void> {
        
        /** The fragment. */
        private WeakReference<SeriesManagementFragment> fragment = null;

        /**
         * Instantiates a new adds the series task.
         *
         * @param fragment the fragment
         */
        public AddShowTask(SeriesManagementFragment fragment) {
            this.fragment = new WeakReference<SeriesManagementFragment>(fragment);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Void doInBackground(ShowBean... params) {
            if (params == null || params.length == 0) {
                throw new IllegalArgumentException("A series bean must be given!");
            }
            AsersApplication asersApplication = (AsersApplication) this.fragment.get().getActivity().getApplication();
            asersApplication.getFinderService().addMyShow(params[0]);
            return null;
        }
    }

}
