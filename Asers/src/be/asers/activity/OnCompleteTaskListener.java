package be.asers.activity;

/**
 * Interface to signal that the task is completed.
 * 
 * @param <Result> the result type
 * @author chesteric31
 */
public interface OnCompleteTaskListener<Result> {

    /**
     * Method called on complete.
     * 
     * @param result the result
     */
    void onComplete(Result result);
}
