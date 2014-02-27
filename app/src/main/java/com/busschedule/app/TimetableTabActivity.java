package com.busschedule.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.busschedule.app.adapter.TimetableListAdapter;
import com.busschedule.app.model.Departure;
import com.busschedule.app.model.Stop;
import com.busschedule.app.server.RoutesAPI;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This activity shows a list of departure times.
 * It is displayed under the TIMETABLES tab.
 * <p/>
 * Created by ecilteodoro on 2/26/14.
 */
public class TimetableTabActivity extends Activity {

    private ProgressDialog progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_tab);

        Bundle params = getIntent().getExtras();

        new FindDeparturesAsyncTask(this).execute(Integer.valueOf(params.getInt("id")));
    }

    @Override
    protected void onDestroy() {
        if (progress != null)
            progress.dismiss();
        super.onDestroy();
    }

    private class FindDeparturesAsyncTask extends AsyncTask<Integer, Void, List<Stop>> {

        private Activity activity;

        public FindDeparturesAsyncTask(Activity activity) {
            this.activity = activity;
        }

        /*
        Display progress dialog before running background task.
         */
        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(activity);
            progress.setMessage(getString(R.string.loading_msg));
            progress.setCancelable(false);
            progress.setIndeterminate(true);
            progress.show();
        }

        /*
        Runs the background task to retrieve data from API.
         */
        protected List doInBackground(Integer... routeId) {
            List<Departure> departures = new ArrayList<Departure>();
            try {
                RoutesAPI api = new RoutesAPI();
                departures = api.findDepartures(routeId[0].intValue());
            } catch (JSONException e1) {
                e1.printStackTrace();
                Toast.makeText(getParent(), getString(R.string.feed_error), Toast.LENGTH_SHORT).show();
            } catch (IOException e2) {
                Toast.makeText(getParent(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                e2.printStackTrace();
            }
            return departures;
        }

        /*
        Dismiss progress dialog and fills the list with retrieved data.
        */
        @Override
        protected void onPostExecute(List departures) {
            if (progress != null)
                progress.dismiss();
            ListView departuresListView = (ListView) findViewById(R.id.timetable_list_view);
            departuresListView.setAdapter(new TimetableListAdapter(activity, departures));
        }
    }

}
