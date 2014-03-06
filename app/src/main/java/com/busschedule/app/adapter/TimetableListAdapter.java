package com.busschedule.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.busschedule.app.model.Departure;

import java.util.List;



public class TimetableListAdapter extends BaseAdapter {

    private Context context;
    private List<Departure> departures;

    public TimetableListAdapter(Context context, List<Departure> departures) {
        this.context = context;
        this.departures = departures;
    }

    @Override
    public int getCount() {
        return departures.size();
    }

    @Override
    public Object getItem(int i) {
        return departures.get(i);
    }

    @Override
    public long getItemId(int i) {
        return departures.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);

        TextView name = (TextView) rowView.findViewById(android.R.id.text1);
        name.setText(departures.get(i).getCalendar() + " - " + departures.get(i).getTime());

        return rowView;
    }
}
