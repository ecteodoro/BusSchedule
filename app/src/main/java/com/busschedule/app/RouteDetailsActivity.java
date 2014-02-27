package com.busschedule.app;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.busschedule.app.adapter.RoutesListAdapter;
import com.busschedule.app.adapter.StopsListAdapter;
import com.busschedule.app.model.Stop;
import com.busschedule.app.server.RoutesAPI;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RouteDetailsActivity extends TabActivity {

    private String routeShortName;
    private String routeLongName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);
        Bundle params = getIntent().getExtras();
        int routeId = params.getInt("id");
        String routeShortName = params.getString("shortName");
        //String routeLongName = params.getString("longName");
        this.setTitle(getString(R.string.route_label) + " " + routeShortName);

        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec stopsTab = tabHost.newTabSpec("t1");
        TabHost.TabSpec timetableTab = tabHost.newTabSpec("t2");

        Intent stopsTabActivity = new Intent(this, StopsTabActivity.class);
        stopsTabActivity.putExtras(params);
        stopsTab.setIndicator("Route");
        stopsTab.setContent(stopsTabActivity);

        Intent timetableTabActivity = new Intent(this, TimetableTabActivity.class);
        timetableTabActivity.putExtras(params);
        timetableTab.setIndicator("Timetable");
        timetableTab.setContent(timetableTabActivity);

        tabHost.addTab(stopsTab);
        tabHost.addTab(timetableTab);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.route_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
