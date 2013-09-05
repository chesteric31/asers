package be.asers.activity;

import java.util.List;

import be.asers.bean.SeriesBean;

/**
 * Interface to signal that the task is completed.
 * 
 * @author chesteric31
 */
public interface OnCompleteTaskListener {

    /**
     * Method called on complete.
     * 
     * @param result the result: list of {@link SeriesBean}
     */
    void onComplete(List<SeriesBean> result);
}
