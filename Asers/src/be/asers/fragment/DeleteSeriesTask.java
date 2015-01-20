package be.asers.fragment;

import be.asers.AsersApplication;
import be.asers.activity.AbstractOnCompleteAsyncTask;
import be.asers.activity.OnCompleteTaskListener;
import be.asers.bean.SeriesBean;

/**
 * Asynchronous task to set INACTIVE a {@link SeriesBean}.
 * 
 * @author chesteric31
 */
class DeleteSeriesTask extends AbstractOnCompleteAsyncTask<SeriesBean, Void, Void> {

    /**
     * 
     */
    private final MySeriesFragment mySeriesFragment;

    public DeleteSeriesTask(MySeriesFragment mySeriesFragment, OnCompleteTaskListener<Void> onCompleteTaskListener) {
        super(onCompleteTaskListener);
        this.mySeriesFragment = mySeriesFragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Void doInBackground(SeriesBean... params) {
        if (params == null || params.length == 0) {
            throw new IllegalArgumentException("A series bean must be given!");
        }
        AsersApplication asersApplication = (AsersApplication) this.mySeriesFragment.getActivity().getApplication();
        asersApplication.getFinderService().deleteMySeries(params[0]);
        return null;
    }
}