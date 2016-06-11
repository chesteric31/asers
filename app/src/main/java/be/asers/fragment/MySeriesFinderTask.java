package be.asers.fragment;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.List;

import be.asers.AsersApplication;
import be.asers.activity.AbstractOnCompleteAsyncTask;
import be.asers.activity.OnCompleteTaskListener;
import be.asers.bean.ShowBean;
import be.asers.service.FinderRemoteService;

/**
 * Asynchronous task to find my {@link ShowBean}.
 * 
 * @author chesteric31
 */
class MySeriesFinderTask extends AbstractOnCompleteAsyncTask<Void, Void, List<ShowBean>> {

    private final MySeriesFragment mySeriesFragment;

    public MySeriesFinderTask(MySeriesFragment mySeriesFragment, OnCompleteTaskListener<List<ShowBean>> onCompleteTaskListener) {
        super(onCompleteTaskListener);
        this.mySeriesFragment = mySeriesFragment;
    }

    @Override
    protected List<ShowBean> doInBackground(Void... params) {
        AsersApplication asersApplication = (AsersApplication) this.mySeriesFragment.getActivity().getApplication();
        List<ShowBean> myShows = asersApplication.getFinderService().findMyShows();
        FinderRemoteService remoteService = asersApplication.getFinderRemoteService();
        for (ShowBean myShow : myShows) {
            Date airDateNextEpisode = remoteService.findAirDateNextEpisode(myShow);
            myShow.setNextEpisodeAirDate(airDateNextEpisode);
            Bitmap bitmap = remoteService.createBitmap(myShow);
            myShow.setCast(bitmap);
        }
        return myShows;
    }
}