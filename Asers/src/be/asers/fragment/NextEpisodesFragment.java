package be.asers.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import be.asers.bean.EpisodeBean;
import be.asers.bean.SeriesBean;
import be.asers.service.FinderService;

/**
 * Fragment for Next Episodes.
 * 
 * @author chesteric31
 */
public class NextEpisodesFragment extends Fragment {

    private static volatile NextEpisodesFragment instance = null;
    private TableLayout nextEpisodesTable;

    /**
     * Constructor.
     */
    public NextEpisodesFragment() {
        // Required empty public constructor
    }

    /**
     * @return singleton instance of {@link NextEpisodesFragment}
     */
    public static final NextEpisodesFragment getInstance() {
        synchronized (NextEpisodesFragment.class) {
            if (NextEpisodesFragment.instance == null) {
                NextEpisodesFragment.instance = new NextEpisodesFragment();
            }
        }
        return NextEpisodesFragment.instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_next_episodes, container, false);
        nextEpisodesTable = (TableLayout) view.findViewById(R.id.next_episodes_table);
        fillTableData(nextEpisodesTable);
        return view;
    }

    /**
     * Fills the spinner with my series with the data found in database.
     * 
     * @param nextEpisodesTable the {@link TableLayout} to use
     */
    private void fillTableData(final TableLayout nextEpisodesTable) {
        final List<EpisodeBean> nextEpisodes = new ArrayList<EpisodeBean>();
        new NextEpisodesFinderTask(new OnCompleteTaskListenerImpl(nextEpisodesTable, nextEpisodes)).execute();
    }

    private final class OnCompleteTaskListenerImpl implements OnCompleteTaskListener<List<EpisodeBean>> {

        private final TableLayout nextEpisodesTable;

        /** The my series. */
        private final List<EpisodeBean> nextEpisodes;

        /**
         * Constructor.
         * 
         * @param nextEpisodesTable
         * @param nextEpisodes
         */
        public OnCompleteTaskListenerImpl(TableLayout nextEpisodesTable, List<EpisodeBean> nextEpisodes) {
            this.nextEpisodesTable = nextEpisodesTable;
            this.nextEpisodes = nextEpisodes;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onComplete(List<EpisodeBean> result) {
            buildTable(result);
        }

        private void buildTable(List<EpisodeBean> result) {
            nextEpisodes.clear();
            clear(nextEpisodesTable);
            nextEpisodes.addAll(result);
            TextView textView = null;
            TableRow tableRow = null;
            if (!nextEpisodes.isEmpty()) {
                for (EpisodeBean episode : nextEpisodes) {
                    textView = new TextView(getActivity());
                    if (episode != null) {
                        Date airDate = episode.getAirDate();
                        String title = episode.getSeason().getSeries().getTitle();
                        textView.setText(title + " : " + airDate);
                        tableRow = new TableRow(getActivity());
                        tableRow.addView(textView);
                        nextEpisodesTable.addView(tableRow);
                    }
                }
            } else {
                clear(nextEpisodesTable);
            }
        }

        private void clear(final TableLayout nextEpisodesTable) {
            for (int i = 0; i < nextEpisodesTable.getChildCount(); i++) {
                if (i > 0) {
                    // don't remove text view
                    TableRow row = (TableRow) nextEpisodesTable.getChildAt(i);
                    nextEpisodesTable.removeView(row);
                }
            }
        }
    }

    /**
     * Asynchronous task to find next episodes.
     * 
     * @author chesteric31
     */
    private class NextEpisodesFinderTask extends AbstractOnCompleteAsyncTask<Void, Void, List<EpisodeBean>> {

        /**
         * Constructor.
         * 
         * @param onCompleteTaskListener the {@link OnCompleteTaskListener} to
         *            use
         */
        public NextEpisodesFinderTask(OnCompleteTaskListener<List<EpisodeBean>> onCompleteTaskListener) {
            super(onCompleteTaskListener);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<EpisodeBean> doInBackground(Void... params) {
            List<EpisodeBean> nextEpisodes = new ArrayList<EpisodeBean>();
            AsersApplication asersApplication = (AsersApplication) getActivity().getApplication();
            FinderService finderService = asersApplication.getFinderService();
            List<SeriesBean> mySeries = finderService.findMySeries();
            if (mySeries != null && !mySeries.isEmpty()) {
                for (SeriesBean mySerie : mySeries) {
                    // finderService.refreshSeries(mySerie);
                    EpisodeBean nextEpisode = finderService.findAirDateNextEpisode(mySerie);
                    nextEpisodes.add(nextEpisode);
                }
            }
            return nextEpisodes;
        }
    }
}
