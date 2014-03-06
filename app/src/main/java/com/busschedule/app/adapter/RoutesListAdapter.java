package com.busschedule.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.busschedule.app.R;
import com.busschedule.app.model.Route;

import java.util.List;

public class RoutesListAdapter extends BaseAdapter {

    private Context context;
    private List<Route> routes;

    public RoutesListAdapter(Context context, List<Route> routes) {
        this.context = context;
        this.routes = routes;
    }

    @Override
    public int getCount() {
        return routes.size();
    }

    @Override
    public Object getItem(int i) {
        return routes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return routes.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(android.R.layout.simple_list_item_2, viewGroup, false);

        TextView shortName = (TextView) rowView.findViewById(android.R.id.text1);
        TextView longName = (TextView) rowView.findViewById(android.R.id.text2);

        shortName.setText(viewGroup.getResources().getString(R.string.route_label) + " " + routes.get(i).getShortName());
        longName.setText(routes.get(i).getLongName());

        return rowView;
    }
}
