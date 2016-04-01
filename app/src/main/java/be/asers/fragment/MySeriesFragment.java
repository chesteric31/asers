package be.asers.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.asers.AsersApplication;
import be.asers.R;
import be.asers.activity.OnCompleteTaskListener;
import be.asers.bean.EpisodeBean;
import be.asers.bean.SeriesBean;
import be.asers.bean.ShowBean;
import be.asers.service.FinderService;

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
        synchronized (MySeriesFragment.class) {
            if (MySeriesFragment.instance == null) {
                MySeriesFragment.instance = new MySeriesFragment();
            }
        }
        return MySeriesFragment.instance;
    }

    /**
     * Refresh.
     */
    public void refresh() {
        getInstance().getFragmentManager().beginTransaction().detach(instance).commit();
        getInstance().getFragmentManager().beginTransaction().attach(instance).commit();
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
     * @param myShowsTable the {@link TableLayout} to use
     */
    private void fillTableData(final TableLayout myShowsTable) {
        final List<ShowBean> myShows = new ArrayList<ShowBean>();
        new MySeriesFinderTask(this, new OnCompleteTaskListenerImpl(myShowsTable, myShows)).execute();
    }

    /**
     * The Class OnCompleteTaskListenerImpl.
     * 
     * @author chesteric31
     */
    private final class OnCompleteTaskListenerImpl implements OnCompleteTaskListener<List<ShowBean>> {

        /** The my series table. */
        private final TableLayout mySeriesTable;

        /** The my series. */
        private final List<ShowBean> myShows;

        /**
         * Instantiates a new on complete task listener impl.
         * 
         * @param mySeriesTable the my series table
         * @param myShows the my series
         */
        private OnCompleteTaskListenerImpl(TableLayout mySeriesTable, List<ShowBean> myShows) {
            this.mySeriesTable = mySeriesTable;
            this.myShows = myShows;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onComplete(List<ShowBean> result) {
            buildTable(result);
        }

        /**
         * Builds the table.
         * 
         * @param result the result
         */
        private void buildTable(List<ShowBean> result) {
            myShows.clear();
            clear(mySeriesTable);
            myShows.addAll(result);
            TextView textView = null;
            ImageView imageView = null;
            TableRow row = null;
            if (!myShows.isEmpty()) {
                AsersApplication asersApplication = (AsersApplication) getActivity().getApplication();
                FinderService finderService = asersApplication.getFinderService();
                for (final ShowBean show : myShows) {
                    textView = new TextView(getActivity());
                    textView.setText(show.toString());
                    imageView = new ImageView(getActivity());
                    //imageView.setImageBitmap(show.getCast());
                    row = new TableRow(getActivity());
                    row.addView(textView);
                    row.addView(buildNextEpisodeAirDate(finderService, show));
                    row.addView(imageView);
                    row.addView(buildRefreshButton(show));
                    row.addView(buildDeleteButton(show));
                    mySeriesTable.addView(row);
                    addLine();
                }
            } else {
                clear(mySeriesTable);
            }
        }

        /**
         * Builds the next episode air date.
         *
         * @param finderService the finder service
         * @param show the show
         * @return the text view
         */
        private TextView buildNextEpisodeAirDate(FinderService finderService, final ShowBean show) {
            TextView textView = new TextView(getActivity());
            EpisodeBean nextEpisode = finderService.findAirDateNextEpisode(show);
            if (nextEpisode != null) {
                Date airDate = nextEpisode.getAirDate();
                textView.setText(getResources().getString(R.string.next_air_date_label) + " : "
                        + SimpleDateFormat.getDateInstance().format(airDate));
            }
            return textView;
        }

        /**
         * Builds the refresh button.
         *
         * @param show the show
         * @return the image button
         */
        private ImageButton buildRefreshButton(final ShowBean show) {
            ImageButton refreshMySeriesButton = new ImageButton(getActivity());
//            refreshMySeriesButton
//                    .setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            refreshMySeriesButton.setImageResource(R.drawable.view_refresh);
            refreshMySeriesButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    new RefreshMySeriesTask(MySeriesFragment.this, new OnCompleteTaskListener<Void>() {

                        @Override
                        public void onComplete(Void result) {
                            // TODO Auto-generated method stub
                        }
                    }).execute(show);
                }
            });
            return refreshMySeriesButton;
        }

        /**
         * Builds the delete button.
         *
         * @param show the show
         * @return the image button
         */
        private ImageButton buildDeleteButton(final ShowBean show) {
            ImageButton deleteMySeriesButton = new ImageButton(getActivity());
//            deleteMySeriesButton
//                    .setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            deleteMySeriesButton.setImageResource(R.drawable.list_remove);
            deleteMySeriesButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    AlertDialog alert = buildConfirmationDialog(show, MySeriesFragment.this.getActivity(),
                            R.string.delete_series_confirmation);
                    alert.show();
                    Toast.makeText(MySeriesFragment.this.getActivity(),
                            MySeriesFragment.this.getString(R.string.add_series_selected) + show, Toast.LENGTH_SHORT)
                            .show();
                }
            });
            return deleteMySeriesButton;
        }

        /**
         * Builds the confirmation dialog.
         *
         * @param show the show
         * @param context the context
         * @param messageId the message id
         * @return the alert dialog
         */
        private AlertDialog buildConfirmationDialog(final ShowBean show, Context context, int messageId) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(messageId);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.delete_series_confirmation_yes, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new DeleteSeriesTask(MySeriesFragment.this, new OnCompleteTaskListener<Void>() {

                        @Override
                        public void onComplete(Void result) {
                            instance.refresh();
                            // getInstance().fillTableData(getInstance().mySeriesTable);
                        }

                    }).execute(show);
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

        /**
         * Adds the line.
         */
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
}
