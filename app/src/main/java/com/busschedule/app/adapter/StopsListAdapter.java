package com.busschedule.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.busschedule.app.model.Stop;

import java.util.List;

public class StopsListAdapter extends BaseAdapter {

    private Context context;
    private List<Stop> stops;

    public StopsListAdapter(Context context, List<Stop> stops) {
        this.context = context;
        this.stops = stops;
    }

    @Override
    public int getCount() {
        return stops.size();
    }

    @Override
    public Object getItem(int i) {
        return stops.get(i);
    }

    @Override
    public long getItemId(int i) {
        return stops.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);

        TextView name = (TextView) rowView.findViewById(android.R.id.text1);

        name.setText(stops.get(i).getSequence() + " - " + stops.get(i).getName());

        return rowView;
    }
}
