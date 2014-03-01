package com.busschedule.app;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class RouteDetailsActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);
        Bundle params = getIntent().getExtras();
        String routeShortName = params.getString("shortName");
        this.setTitle(getString(R.string.route_label) + " " + routeShortName);

        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec stopsTab = tabHost.newTabSpec("t1");
        TabHost.TabSpec timetableTab = tabHost.newTabSpec("t2");

        Intent stopsTabActivity = new Intent(this, StopsTabActivity.class);
        stopsTabActivity.putExtras(params);
        stopsTab.setIndicator(getString(R.string.stops_tab));
        stopsTab.setContent(stopsTabActivity);

        Intent timetableTabActivity = new Intent(this, TimetableTabActivity.class);
        timetableTabActivity.putExtras(params);
        timetableTab.setIndicator(getString(R.string.timetable_tab));
        timetableTab.setContent(timetableTabActivity);

        tabHost.addTab(stopsTab);
        tabHost.addTab(timetableTab);

    }

}
