package be.asers.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

import be.asers.AsersApplication;
import be.asers.bean.ShowBean;
import be.asers.service.FinderRemoteService;

public class ShowBeanListAdapter extends ArrayAdapter<ShowBean> implements Filterable {

    private LayoutInflater layoutInflater;

    public ShowBeanListAdapter(final Context context) {
        super(context, -1);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final TextView textView;
        if (convertView != null) {
            textView = (TextView) convertView;
        } else {
            textView = (TextView) layoutInflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }

        textView.setText(createFormattedShow(getItem(position)));
        return textView;
    }

    private String createFormattedShow(final ShowBean show) {
        return show.getName();
    }

    @Override
    public Filter getFilter() {
        Filter showBeanFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(final CharSequence keywords) {
                final FilterResults filteredResults = new FilterResults();
                if (keywords != null) {
                    FinderRemoteService service = ((AsersApplication) ((Activity) getContext()).getApplication()).getFinderRemoteService();
                    final List<ShowBean> shows = service.findShowsByKeywords(keywords.toString());
                    filteredResults.values = shows;
                    filteredResults.count = shows.size();
                }
                return filteredResults;
            }

            @Override
            protected void publishResults(final CharSequence contraint, final FilterResults results) {
                clear();
                if (results.values != null) {
                    for (ShowBean show : (List<ShowBean>) results.values) {
                        add(show);
                    }
                    if (results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            }

            @Override
            public CharSequence convertResultToString(final Object resultValue) {
                return resultValue == null ? "" : ((ShowBean) resultValue).getName();
            }
        };
        return showBeanFilter;
    }
}
