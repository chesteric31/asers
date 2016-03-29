package be.asers.fragment;

import be.asers.AsersApplication;
import be.asers.activity.AbstractOnCompleteAsyncTask;
import be.asers.activity.OnCompleteTaskListener;
import be.asers.bean.SeriesBean;

/**
 * Asynchronous task to refresh my {@link SeriesBean}.
 * 
 * @author chesteric31
 */
class RefreshMySeriesTask extends AbstractOnCompleteAsyncTask<SeriesBean, Void, Void> {

    /** The my series fragment. */
    private final MySeriesFragment mySeriesFragment;

    /**
     * Constructor.
     *
     * @param mySeriesFragment TODO
     * @param onCompleteTaskListener the {@link OnCompleteTaskListener} to
     *            use
     */
    public RefreshMySeriesTask(MySeriesFragment mySeriesFragment, OnCompleteTaskListener<Void> onCompleteTaskListener) {
        super(onCompleteTaskListener);
        this.mySeriesFragment = mySeriesFragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Void doInBackground(SeriesBean... mySeries) {
        AsersApplication asersApplication = (AsersApplication) this.mySeriesFragment.getActivity().getApplication();
        asersApplication.getFinderService().refreshSeries(mySeries[0]);
        return null;
    }
}