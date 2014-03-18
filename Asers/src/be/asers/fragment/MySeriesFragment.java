package be.asers.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
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

    private static volatile MySeriesFragment instance = null;
    private TableLayout mySeriesTable;

    /**
     * Constructor.
     */
    public MySeriesFragment() {
        // Required empty public constructor
    }

    /**
     * @return singleton instance of {@link MySeriesFragment}
     */
    public static final MySeriesFragment getInstance() {
        if (MySeriesFragment.instance == null) {
            synchronized (MySeriesFragment.class) {
                if (MySeriesFragment.instance == null) {
                    MySeriesFragment.instance = new MySeriesFragment();
                }
            }
        }
        return MySeriesFragment.instance;
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
                ImageView imageView = null;
                TableRow tableRow = null;
                if (!mySeries.isEmpty()) {
                    for (SeriesBean series : mySeries) {
                        textView = new TextView(getActivity());
                        textView.setText(series.toString());
                        imageView = new ImageView(getActivity());
                        imageView.setImageBitmap(series.getCast());
                        tableRow = new TableRow(getActivity());
                        tableRow.addView(textView);
                        tableRow.addView(imageView);
                        mySeriesTable.addView(tableRow);
                        View redLineView = new View(getActivity());
                        redLineView.setLayoutParams(new LayoutParams(100, 2));
                        redLineView.setBackgroundColor(getResources().getColor(R.color.row_separator_color));
                        mySeriesTable.addView(redLineView);
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
