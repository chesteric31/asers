package be.asers.fragment;

import be.asers.AsersApplication;
import be.asers.activity.AbstractOnCompleteAsyncTask;
import be.asers.activity.OnCompleteTaskListener;
import be.asers.bean.ShowBean;

/**
 * Asynchronous task to set INACTIVE a {@link ShowBean}.
 * 
 * @author chesteric31
 */
class DeleteSeriesTask extends AbstractOnCompleteAsyncTask<ShowBean, Void, Void> {

    /** The my series fragment. */
    private final MySeriesFragment mySeriesFragment;

    /**
     * Instantiates a new delete series task.
     *
     * @param mySeriesFragment the my series fragment
     * @param onCompleteTaskListener the on complete task listener
     */
    public DeleteSeriesTask(MySeriesFragment mySeriesFragment, OnCompleteTaskListener<Void> onCompleteTaskListener) {
        super(onCompleteTaskListener);
        this.mySeriesFragment = mySeriesFragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Void doInBackground(ShowBean... params) {
        if (params == null || params.length == 0) {
            throw new IllegalArgumentException("A series bean must be given!");
        }
        AsersApplication asersApplication = (AsersApplication) this.mySeriesFragment.getActivity().getApplication();
        asersApplication.getFinderService().deleteMyShow(params[0]);
        return null;
    }
}