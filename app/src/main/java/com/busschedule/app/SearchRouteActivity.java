package com.busschedule.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static com.busschedule.app.server.RoutesAPI.STOP_NAME;

/**
 * This activity displays a search field and a list
 * of routes that result from the search.
 */
public class SearchRouteActivity extends Activity {

    private static final double FLORIPA_LATITUDE = -27.593500;
    private static final double FLORIPA_LONGITUDE = -48.558540;
    private static final float INITIAL_ZOOM_LEVEL = 13;
    private static final String LOG_TAG = "BusSchedule.app";
    private static final String LOG_MSG = "Location not found";

    private EditText stopNameField;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_route);
        stopNameField = (EditText) findViewById(R.id.stop_name_field);

        drawMap();
    }

    /*
    Dismiss the progress dialog if activity is stopped.
    */
    @Override
    protected void onStop() {
        if (progress != null)
            progress.dismiss();
        super.onStop();
    }

    /*
    This method is triggered by the search button and
    starts a background task to retrieve JSON data from a REST API.
    */
    public void findRoutes(View view) {
        String stopName = stopNameField.getText().toString();

        hideKeyboard();

        Intent routeListActivity = new Intent(this, RouteListActivity.class);
        Bundle params = new Bundle();
        params.putString(STOP_NAME, stopName);
        routeListActivity.putExtras(params);
        this.startActivity(routeListActivity);
    }

    /*
    Hides the soft keyboard when user clicks the search button.
     */
    private void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void drawMap() {
        GoogleMap mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);

        LatLng floripa = new LatLng(FLORIPA_LATITUDE, FLORIPA_LONGITUDE);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(floripa, INITIAL_ZOOM_LEVEL));

        mMap.addMarker(new MarkerOptions()
                .title(getString(R.string.floripa))
                .snippet(getString(R.string.bus_routes_timetable))
                .position(floripa));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                new ReverseGeocodingTask(SearchRouteActivity.this).execute(latLng);
            }
        });
    }

    private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {

        Activity activity;

        public ReverseGeocodingTask(Activity activity) {
            super();
            this.activity = activity;
        }

        /*
        Display progress dialog before running background task.
         */
        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(activity);
            progress.setMessage(getString(R.string.finding_location));
            progress.setCancelable(false);
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected String doInBackground(LatLng... latLngs) {
            Geocoder geocoder = new Geocoder(activity.getBaseContext());
            double latitude = latLngs[0].latitude;
            double longitude = latLngs[0].longitude;
            List<Address> addresses;
            String street = "";
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    street = addresses.get(0).getAddressLine(0);
                    //this trick works only for brazilian address format
                    street = street.split(",")[0];
                }
            } catch (IOException e) {
                Log.e(LOG_TAG,  LOG_MSG);
            }
            return street;
        }

        @Override
        protected void onPostExecute(String street) {
            if (progress != null)
                progress.dismiss();
            stopNameField.setText(street);
        }

    }
}
