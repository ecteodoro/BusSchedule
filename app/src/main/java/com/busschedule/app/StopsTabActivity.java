package com.busschedule.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.busschedule.app.adapter.StopsListAdapter;
import com.busschedule.app.model.Stop;
import com.busschedule.app.server.RoutesAPI;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;


/**
 * This activity shows a list of streets (stops).
 * It is displayed under the ROUTES tab.
 * <p/>
 * Created by ecilteodoro on 2/26/14.
 */
public class StopsTabActivity extends Activity {

    private ProgressDialog progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops_tab);

        Bundle params = getIntent().getExtras();

        new FindStopsAsyncTask(this).execute(Integer.valueOf(params.getInt("id")));
    }

    @Override
    protected void onStop() {
        if (progress != null)
            progress.dismiss();
        super.onStop();
    }

    private class FindStopsAsyncTask extends AsyncTask<Integer, Void, List<Stop>> {

        private Activity activity;

        public FindStopsAsyncTask(Activity activity) {
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
        protected List<Stop> doInBackground(Integer... routeId) {
            List<Stop> stops = null;
            try {
                RoutesAPI api = RoutesAPI.getInstance();
                stops = api.findStops(routeId[0].intValue());
            } catch (JSONException e) {
                Toast.makeText(getParent(), getString(R.string.feed_error), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getParent(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
            return stops;
        }

        /*
       Dismiss progress dialog and fills the list with retrieved data.
        */
        @Override
        protected void onPostExecute(List stops) {
            if (progress != null)
                progress.dismiss();
            ListView stopListView = (ListView) activity.findViewById(R.id.stops_list_view);
            stopListView.setAdapter(new StopsListAdapter(activity, stops));
        }
    }

}
