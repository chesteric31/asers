package be.asers.fragment;

import be.asers.AsersApplication;
import be.asers.activity.AbstractOnCompleteAsyncTask;
import be.asers.activity.OnCompleteTaskListener;
import be.asers.bean.ShowBean;

/**
 * Asynchronous task to refresh my {@link ShowBean}.
 * 
 * @author chesteric31
 */
class RefreshMySeriesTask extends AbstractOnCompleteAsyncTask<ShowBean, Void, Void> {

    /** The my series fragment. */
    private final MySeriesFragment mySeriesFragment;

    /**
     * Constructor.
     *
     * @param mySeriesFragment
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
    protected Void doInBackground(ShowBean... myShow) {
        AsersApplication asersApplication = (AsersApplication) this.mySeriesFragment.getActivity().getApplication();
        asersApplication.getFinderService().refreshShow(myShow[0]);
        return null;
    }
}