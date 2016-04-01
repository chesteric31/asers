package be.asers.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.asers.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class TvMazeActivityFragment extends Fragment {

    public TvMazeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_maze, container, false);
    }
}
