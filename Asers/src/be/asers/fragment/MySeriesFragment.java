package be.asers.fragment;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import be.asers.AsersApplication;
import be.asers.R;
import be.asers.activity.AbstractOnCompleteAsyncTask;
import be.asers.activity.OnCompleteTaskListener;
import be.asers.bean.SeriesBean;

/**
 * 
 * 
 * 
 * @author chesteric31
 */
public class MySeriesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TableLayout mySeriesTable;

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     * 
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MySeriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MySeriesFragment newInstance(String param1, String param2) {
        MySeriesFragment fragment = new MySeriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MySeriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_series, container, false);
        mySeriesTable = (TableLayout) view.findViewById(R.id.my_series_table);
        fillTableData(mySeriesTable);
        return view;
    }

    /**
     * Fills the spinner with my series with the data found in database.
     * 
     * @param mySeriesTable the {@link TableLayout} to use
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
                        textView = new TextView(getActivity());
                        textView.setText(series.toString());
                        tableRow = new TableRow(getActivity());
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated to
     * the activity and potentially other fragments contained in that activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
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
            AsersApplication asersApplication = (AsersApplication) getActivity().getApplication();
            return asersApplication.getFinderService().findMySeries();
        }
    }

}
