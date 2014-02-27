package com.busschedule.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.busschedule.app.adapter.RoutesListAdapter;
import com.busschedule.app.model.Route;
import com.busschedule.app.server.RoutesAPI;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This activity displays a search field and a list
 * of routes that result from the search.
 *
 */
public class SearchRouteActivity extends Activity {

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_route);
    }

    /*
    Dismiss the progress dialog if activity is destroyed.
    */
    @Override
    protected void onDestroy() {
        if (progress != null)
            progress.dismiss();
        super.onDestroy();
    }

    /*
    This method is triggered by the search button and
    starts a background task to retrieve JSON data from a REST API.
    */
    public void findRoutes(View view) {
        EditText stopNameField = (EditText)findViewById(R.id.stop_name_field);
        String stopName = stopNameField.getText().toString();
        hideKeyboard();
        new FindRouteAsyncTask(this).execute(stopName);
    }

    /*
    Hides the soft keyboard when user clicks the search button.
     */
    private void hideKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    private class FindRouteAsyncTask extends AsyncTask<String, Void, List<Route>> {

        private Activity activity;

        public FindRouteAsyncTask(Activity activity){
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
        protected List doInBackground(String... stopName) {
            List<Route> routes = new ArrayList<Route>();
            try {
                RoutesAPI api = new RoutesAPI();
                routes = api.findRoutes(stopName[0]);
            } catch (JSONException e1) {
                e1.printStackTrace();
                Toast.makeText(getParent(), getString(R.string.feed_error), Toast.LENGTH_SHORT).show();
            } catch (IOException e2) {
                Toast.makeText(getParent(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                e2.printStackTrace();
            }
            return routes;
        }

        /*
        Dismiss progress dialog, fills the list with retrieved data
        and define the behavior when a list item is selected.
         */
        @Override
        protected void onPostExecute(List routes) {
            if (progress != null)
                progress.dismiss();
            ListView routeListView = (ListView)findViewById(R.id.routes_list_view);
            routeListView.setAdapter(new RoutesListAdapter(activity, routes));
            routeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Route selectedRoute = (Route)adapterView.getItemAtPosition(i);
                    Intent showRouteDetailsActivity = new Intent(activity, RouteDetailsActivity.class);
                    Bundle params = new Bundle();
                    params.putInt("id", selectedRoute.getId());
                    params.putString("shortName", selectedRoute.getShortName());
                    params.putString("longName", selectedRoute.getLongName());
                    showRouteDetailsActivity.putExtras(params);
                    startActivity(showRouteDetailsActivity);
                }
            });
        }
    }
}
