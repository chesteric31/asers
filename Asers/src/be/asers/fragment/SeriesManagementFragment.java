package be.asers.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import be.asers.R;
import be.asers.activity.AddSeriesActivity;
import be.asers.activity.DeleteSeriesActivity;

/**
 * Fragment for Series management.
 * 
 * @author chesteric31
 */
public class SeriesManagementFragment extends Fragment {

    private static volatile SeriesManagementFragment instance = null;

    /**
     * Constructor.
     */
    public SeriesManagementFragment() {
        // Required empty public constructor
    }

    /**
     * @return singleton instance of {@link SeriesManagementFragment}
     */
    public static final SeriesManagementFragment getInstance() {
        if (SeriesManagementFragment.instance == null) {
            synchronized (SeriesManagementFragment.class) {
                if (SeriesManagementFragment.instance == null) {
                    SeriesManagementFragment.instance = new SeriesManagementFragment();
                }
            }
        }
        return SeriesManagementFragment.instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_series_management, container, false);
        addListenerOnAddSeriesButon(view);
        return view;
    }

    /**
     * Adds listener on add series button.
     * 
     * @param view the {@link View} to use
     */
    private void addListenerOnAddSeriesButon(View view) {
        Button addSeriesButton = (Button) view.findViewById(R.id.add_series_button);
        addSeriesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent addSeriesActivity = new Intent(getActivity(), AddSeriesActivity.class);
                startActivityForResult(addSeriesActivity, 0);
            }
        });
        Button deleteSeriesButton = (Button) view.findViewById(R.id.delete_series_button);
        deleteSeriesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent deleteSeriesActivity = new Intent(getActivity(), DeleteSeriesActivity.class);
                startActivityForResult(deleteSeriesActivity, 0);
            }
        });
    }

}
