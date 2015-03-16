package be.asers.activity;

import android.os.AsyncTask;

/**
 * Abstract class with {@link OnCompleteTaskListener} capability.
 *
 * @author chesteric31
 * @param <Params> the parameters type
 * @param <Progress> the progress type
 * @param <Result> the result type
 */
public abstract class AbstractOnCompleteAsyncTask<Params, Progress, Result>
        extends AsyncTask<Params, Progress, Result> {

    /** The on complete task listener. */
    private OnCompleteTaskListener<Result> onCompleteTaskListener;

    /**
     * Constructor.
     * 
     * @param onCompleteTaskListener the {@link OnCompleteTaskListener} to use
     */
    public AbstractOnCompleteAsyncTask(OnCompleteTaskListener<Result> onCompleteTaskListener) {
        this.onCompleteTaskListener = onCompleteTaskListener;
    };

    /**
     * {@inheritDoc}
     */
    @Override
    protected abstract Result doInBackground(Params... params);

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(Result result) {
        onCompleteTaskListener.onComplete(result);
    }

}
