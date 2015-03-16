package be.asers.activity;

/**
 * Interface to signal that the task is completed.
 *
 * @author chesteric31
 * @param <Result> the result type
 */
public interface OnCompleteTaskListener<Result> {

    /**
     * Method called on complete.
     * 
     * @param result the result
     */
    void onComplete(Result result);
}
