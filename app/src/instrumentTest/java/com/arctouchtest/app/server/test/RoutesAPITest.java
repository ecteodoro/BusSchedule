package com.arctouchtest.app.server.test;

import android.test.AndroidTestCase;
import android.util.Log;

import com.busschedule.app.model.Calendar;
import com.busschedule.app.model.Departure;
import com.busschedule.app.model.Route;
import com.busschedule.app.model.Stop;
import com.busschedule.app.server.RoutesAPI;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ecilteodoro on 2/25/14.
 */
public class RoutesAPITest extends AndroidTestCase {

    private static String TAG = "**** ArcTouch Test ***";

    public void testFindRoutes() {
        RoutesAPI api = new RoutesAPI();
        List<Route> routes = new ArrayList<Route>();

        try {
            routes = api.findRoutes("linhares");
        } catch (JSONException e) {
            Log.i(TAG, "json exception");
        } catch (IOException e) {
            Log.i(TAG, "io exception");
        }

        assertNotNull(routes);
        assertTrue(routes.size() > 0);

        assertEquals(routes.get(0).getShortName(), "131");
        assertEquals(routes.get(1).getShortName(), "133");

       for (Route route: routes) {
            System.out.println(route.getId());
            System.out.println(route.getShortName());
            System.out.println(route.getLongName());
        }
    }

    public void testFindDepartures() {
        RoutesAPI api = new RoutesAPI();
        List<Departure> departures = new ArrayList<Departure>();

        try {
            departures = api.findDepartures(22);
        } catch (JSONException e) {
            Log.i(TAG, "json exception");
        } catch (IOException e) {
            Log.i(TAG, "io exception");
        }

        assertNotNull(departures);
        assertTrue(departures.size() > 0);

        assertEquals(departures.get(0).getId(), 208);
        assertEquals(departures.get(42).getId(), 246);
        assertEquals(departures.get(0).getCalendar().name(), Calendar.WEEKDAY.name());
        assertEquals(departures.get(42).getCalendar().name(), Calendar.SATURDAY.name());

        for (Departure departure: departures) {
            System.out.println(departure.getId());
            System.out.println(departure.getCalendar().name());
            System.out.println(departure.getTime());
        }
    }

    public void testFindStops() {
        RoutesAPI api = new RoutesAPI();
        List<Stop> stops = new ArrayList<Stop>();

        try {
            stops = api.findStops(22);
        } catch (JSONException e) {
            Log.i(TAG, "json exception");
        } catch (IOException e) {
            Log.i(TAG, "io exception");
        }

        assertNotNull(stops);
        assertTrue(stops.size() > 0);

        assertEquals(stops.get(0).getId(), 47);
        assertEquals(stops.get(0).getName(), "TICEN");
        assertEquals(stops.get(0).getSequence(), 1);
        assertEquals(stops.get(0).getRouteId(), 22);

        for (Stop stop: stops) {
            System.out.println(stop.getId());
            System.out.println(stop.getName());
            System.out.println(stop.getSequence());
            System.out.println(stop.getRouteId());
        }
    }
}
