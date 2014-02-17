package be.asers.fragment;

import java.util.ArrayList;
import java.util.Date;
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
import be.asers.bean.EpisodeBean;
import be.asers.bean.SeriesBean;
import be.asers.service.FinderService;

/**
 * 
 * 
 * 
 * @author chesteric31
 */
public class NextEpisodesFragment extends Fragment {
    
    // TODO : Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO : Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TableLayout nextEpisodesTable;

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     * 
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NextEpisodesFragment.
     */
    // TODO : Rename and change types and number of parameters
    public static NextEpisodesFragment newInstance(String param1, String param2) {
        NextEpisodesFragment fragment = new NextEpisodesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Constructor.
     */
    public NextEpisodesFragment() {
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
        new NextEpisodesFinderTask(new OnCompleteTaskListener<List<EpisodeBean>>() {

            /**
             * {@inheritDoc}
             */
            @Override
            public void onComplete(List<EpisodeBean> result) {
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
        }).execute();
    }

    // TODO : Rename method, update argument and hook method into UI event
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
        // TODO : Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                    EpisodeBean nextEpisode = finderService.findAirDateNextEpisode(mySerie);
                    nextEpisodes.add(nextEpisode);
                }
            }
            return nextEpisodes;
        }
    }
}
