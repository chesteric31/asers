package be.asers.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import be.asers.AsersApplication;
import be.asers.R;
import be.asers.activity.AbstractOnCompleteAsyncTask;
import be.asers.activity.OnCompleteTaskListener;
import be.asers.bean.SeriesBean;

/**
 * Fragment for My Series.
 * 
 * @author chesteric31
 */
public class MySeriesFragment extends Fragment {

    /** The instance. */
    private static volatile MySeriesFragment instance = null;

    /** The my series table. */
    private TableLayout mySeriesTable;

    /**
     * Constructor.
     */
    public MySeriesFragment() {
        // Required empty public constructor
    }

    /**
     * Gets the single instance of MySeriesFragment.
     * 
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
        new MySeriesFinderTask(new OnCompleteTaskListenerImpl(mySeriesTable, mySeries)).execute();
    }

    /**
     * The Class OnCompleteTaskListenerImpl.
     * 
     * @author chesteric31
     */
    private final class OnCompleteTaskListenerImpl implements OnCompleteTaskListener<List<SeriesBean>> {

        /** The my series table. */
        private final TableLayout mySeriesTable;

        /** The my series. */
        private final List<SeriesBean> mySeries;

        /**
         * Instantiates a new on complete task listener impl.
         * 
         * @param mySeriesTable the my series table
         * @param mySeries the my series
         */
        private OnCompleteTaskListenerImpl(TableLayout mySeriesTable, List<SeriesBean> mySeries) {
            this.mySeriesTable = mySeriesTable;
            this.mySeries = mySeries;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onComplete(List<SeriesBean> result) {
            buildTable(result);
        }

        /**
         * Builds the table.
         * 
         * @param result the result
         */
        private void buildTable(List<SeriesBean> result) {
            mySeries.clear();
            clear(mySeriesTable);
            mySeries.addAll(result);
            TextView textView = null;
            ImageView imageView = null;
            TableRow row = null;
            if (!mySeries.isEmpty()) {
                for (final SeriesBean series : mySeries) {
                    textView = new TextView(getActivity());
                    textView.setText(series.toString());
                    imageView = new ImageView(getActivity());
                    imageView.setImageBitmap(series.getCast());
                    row = new TableRow(getActivity());
                    row.addView(textView);
                    row.addView(imageView);
                    row.addView(buildRefreshButton(series));
                    row.addView(buildDeleteButton(series));
                    mySeriesTable.addView(row);
                    addLine();
                }
            } else {
                clear(mySeriesTable);
            }
        }

        private Button buildRefreshButton(final SeriesBean series) {
            Button refreshMySeriesButton = new Button(getActivity());
            refreshMySeriesButton.setText(getResources().getString(R.string.refresh_my_series_button));
            refreshMySeriesButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    new RefreshMySeriesTask(new OnCompleteTaskListener<Void>() {

                        @Override
                        public void onComplete(Void result) {
                            // TODO Auto-generated method stub
                            // throw new UnsupportedOperationException();
                        }
                    }).execute(series);
                }
            });
            return refreshMySeriesButton;
        }

        private Button buildDeleteButton(final SeriesBean series) {
            Button deleteMySeriesButton = new Button(getActivity());
            deleteMySeriesButton.setText(getResources().getString(R.string.delete_series_button));
            deleteMySeriesButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    AlertDialog alert = buildConfirmationDialog(series, MySeriesFragment.this.getActivity(),
                            R.string.delete_series_confirmation);
                    alert.show();
                    Toast.makeText(MySeriesFragment.this.getActivity(),
                            MySeriesFragment.this.getString(R.string.add_series_selected) + series, Toast.LENGTH_SHORT)
                            .show();
                }
            });
            return deleteMySeriesButton;
        }

        private AlertDialog buildConfirmationDialog(final SeriesBean series, Context context, int messageId) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(messageId);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.delete_series_confirmation_yes, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new DeleteSeriesTask(new OnCompleteTaskListener<Void>() {

                        @Override
                        public void onComplete(Void result) {
                            refresh();
//                            getInstance().fillTableData(getInstance().mySeriesTable);
                        }

                        private void refresh() {
                            getInstance().getFragmentManager().beginTransaction().detach(instance).commit();
                            getInstance().getFragmentManager().beginTransaction().attach(instance).commit();
                        }
                    }).execute(series);
                }
            });
            builder.setNegativeButton(R.string.delete_series_confirmation_no, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            return alert;
        }

        private void addLine() {
            View redLineView = new View(getActivity());
            redLineView.setLayoutParams(new LayoutParams(100, 2));
            redLineView.setBackgroundColor(getResources().getColor(R.color.row_separator_color));
            mySeriesTable.addView(redLineView);
        }

        /**
         * Clear.
         * 
         * @param mySeriesTable the my series table
         */
        private void clear(final TableLayout mySeriesTable) {
            for (int i = 0; i < mySeriesTable.getChildCount(); i++) {
                if (i > 0) {
                    // don't remove text view
                    TableRow row = (TableRow) mySeriesTable.getChildAt(i);
                    mySeriesTable.removeView(row);
                }
            }
        }
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

    /**
     * Asynchronous task to refresh my {@link SeriesBean}.
     * 
     * @author chesteric31
     */
    private class RefreshMySeriesTask extends AbstractOnCompleteAsyncTask<SeriesBean, Void, Void> {

        /**
         * Constructor.
         * 
         * @param onCompleteTaskListener the {@link OnCompleteTaskListener} to
         *            use
         */
        public RefreshMySeriesTask(OnCompleteTaskListener<Void> onCompleteTaskListener) {
            super(onCompleteTaskListener);
        }

        @Override
        protected Void doInBackground(SeriesBean... mySeries) {
            AsersApplication asersApplication = (AsersApplication) getActivity().getApplication();
            asersApplication.getFinderService().refreshSeries(mySeries[0]);
            return null;
        }
    }

    /**
     * Asynchronous task to set INACTIVE a {@link SeriesBean}.
     * 
     * @author chesteric31
     */
    private class DeleteSeriesTask extends AbstractOnCompleteAsyncTask<SeriesBean, Void, Void> {

        public DeleteSeriesTask(OnCompleteTaskListener<Void> onCompleteTaskListener) {
            super(onCompleteTaskListener);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Void doInBackground(SeriesBean... params) {
            if (params == null || params.length == 0) {
                throw new IllegalArgumentException("A series bean must be given!");
            }
            AsersApplication asersApplication = (AsersApplication) getActivity().getApplication();
            asersApplication.getFinderService().deleteMySeries(params[0]);
            return null;
        }
    }
}
