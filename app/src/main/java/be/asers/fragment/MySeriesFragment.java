package be.asers.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.asers.R;
import be.asers.activity.OnCompleteTaskListener;
import be.asers.bean.ShowBean;

/**
 * Fragment for My Series.
 * 
 * @author chesteric31
 */
public class MySeriesFragment extends Fragment {

    /** The instance. */
    private static volatile MySeriesFragment instance = null;

    /** The my series table. */
    private TableLayout myShowsTable;

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
        myShowsTable = (TableLayout) view.findViewById(R.id.my_series_table);
        fillTableData(myShowsTable);
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

        /** The my shows table. */
        private final TableLayout myShowsTable;

        /** The my series. */
        private final List<ShowBean> myShows;

        /**
         * Instantiates a new on complete task listener impl.
         * 
         * @param myShowsTable the my series table
         * @param myShows the my series
         */
        private OnCompleteTaskListenerImpl(TableLayout myShowsTable, List<ShowBean> myShows) {
            this.myShowsTable = myShowsTable;
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
            clear(myShowsTable);
            myShows.addAll(result);
            TextView textView = null;
            ImageView imageView = null;
            TableRow row = null;
            if (!myShows.isEmpty()) {
                for (final ShowBean show : myShows) {
                    row = new TableRow(getActivity());
                    imageView = buildImageView(show);
                    row.addView(imageView);
                    textView = buildTextView(show);
                    row.addView(textView);
                    Date airDate = show.getNextEpisodeAirDate();
                    if (airDate != null) {
                        TextView dateView = buildDateView(airDate);
                        row.addView(dateView);
                    }
                    row.addView(buildDeleteButton(show));
                    myShowsTable.addView(row);
                    addLine();
                }
            } else {
                clear(myShowsTable);
            }
        }

        private ImageView buildImageView(ShowBean show) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageBitmap(show.getCast());
            return imageView;
        }

        private TextView buildTextView(ShowBean show) {
            TextView textView = new TextView(getActivity());
            textView.setText(show.toString());
            return textView;
        }

        private TextView buildDateView(Date airDate) {
            String nextEpisodeAirDate = DateFormat.getDateTimeInstance().format(airDate);
            TextView dateView = new TextView(getActivity());
            dateView.setText(getResources().getString(R.string.next_air_date_label, nextEpisodeAirDate));
            return dateView;
        }

        /**
         * Builds the delete button.
         *
         * @param show the show
         * @return the image button
         */
        private Button buildDeleteButton(final ShowBean show) {
            Button deleteMySeriesButton = new Button(getActivity());
            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf" );
            deleteMySeriesButton.setTypeface(font);
            deleteMySeriesButton.setText(R.string.remove_icon);
            deleteMySeriesButton.setBackgroundResource(0);
            deleteMySeriesButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    AlertDialog alert = buildConfirmationDialog(show, MySeriesFragment.this.getActivity(),
                            R.string.delete_series_confirmation);
                    alert.show();
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
            return builder.create();
        }

        /**
         * Adds the line.
         */
        private void addLine() {
            View redLineView = new View(getActivity());
            redLineView.setLayoutParams(new LayoutParams(500, 2));
            redLineView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.row_separator_color));
            myShowsTable.addView(redLineView);
        }

        /**
         * Clear.
         * 
         * @param myShowsTable the my series table
         */
        private void clear(final TableLayout myShowsTable) {
            for (int i = 0; i < myShowsTable.getChildCount(); i++) {
                if (i > 0) {
                    // don't remove text view
                    TableRow row = (TableRow) myShowsTable.getChildAt(i);
                    myShowsTable.removeView(row);
                }
            }
        }
    }
}
