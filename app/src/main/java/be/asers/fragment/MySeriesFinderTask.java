package be.asers.fragment;

import java.util.List;

import be.asers.AsersApplication;
import be.asers.activity.AbstractOnCompleteAsyncTask;
import be.asers.activity.OnCompleteTaskListener;
import be.asers.bean.SeriesBean;
import be.asers.bean.ShowBean;

/**
 * Asynchronous task to find my {@link SeriesBean}.
 * 
 * @author chesteric31
 */
class MySeriesFinderTask extends AbstractOnCompleteAsyncTask<Void, Void, List<ShowBean>> {

    /** The my series fragment. */
    private final MySeriesFragment mySeriesFragment;

    /**
     * Constructor.
     *
     * @param mySeriesFragment
     * @param onCompleteTaskListener the {@link OnCompleteTaskListener} to
     *            use
     */
    public MySeriesFinderTask(MySeriesFragment mySeriesFragment, OnCompleteTaskListener<List<ShowBean>> onCompleteTaskListener) {
        super(onCompleteTaskListener);
        this.mySeriesFragment = mySeriesFragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<ShowBean> doInBackground(Void... params) {
        AsersApplication asersApplication = (AsersApplication) this.mySeriesFragment.getActivity().getApplication();
        return asersApplication.getFinderService().findMyShows();
    }
}