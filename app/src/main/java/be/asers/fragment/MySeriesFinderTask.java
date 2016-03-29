package be.asers.fragment;

import java.util.List;

import be.asers.AsersApplication;
import be.asers.activity.AbstractOnCompleteAsyncTask;
import be.asers.activity.OnCompleteTaskListener;
import be.asers.bean.SeriesBean;

/**
 * Asynchronous task to find my {@link SeriesBean}.
 * 
 * @author chesteric31
 */
class MySeriesFinderTask extends AbstractOnCompleteAsyncTask<Void, Void, List<SeriesBean>> {

    /** The my series fragment. */
    private final MySeriesFragment mySeriesFragment;

    /**
     * Constructor.
     *
     * @param mySeriesFragment TODO
     * @param onCompleteTaskListener the {@link OnCompleteTaskListener} to
     *            use
     */
    public MySeriesFinderTask(MySeriesFragment mySeriesFragment, OnCompleteTaskListener<List<SeriesBean>> onCompleteTaskListener) {
        super(onCompleteTaskListener);
        this.mySeriesFragment = mySeriesFragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<SeriesBean> doInBackground(Void... params) {
        AsersApplication asersApplication = (AsersApplication) this.mySeriesFragment.getActivity().getApplication();
        return asersApplication.getFinderService().findMySeries();
    }
}