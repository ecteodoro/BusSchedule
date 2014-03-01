package com.arctouchtest.app.server.test;

import android.test.AndroidTestCase;

import com.busschedule.app.model.Calendar;
import com.busschedule.app.model.Departure;
import com.busschedule.app.model.Route;
import com.busschedule.app.model.Stop;
import com.busschedule.app.server.RoutesAPI;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by ecilteodoro on 2/25/14.
 */
public class RoutesAPITest extends AndroidTestCase {

    public void testFindRoutes() throws IOException, JSONException {
        RoutesAPI api = RoutesAPI.getInstance();
        List<Route> routes = api.findRoutes("linhares");

        assertNotNull(routes);
        assertTrue(routes.size() > 0);

        assertEquals(routes.get(0).getShortName(), "131");
        assertEquals(routes.get(1).getShortName(), "133");

    }

    public void testFindDepartures() throws IOException, JSONException {
        RoutesAPI api = RoutesAPI.getInstance();
        List<Departure> departures = api.findDepartures(22);

        assertNotNull(departures);
        assertTrue(departures.size() > 0);

        assertEquals(departures.get(0).getId(), 208);
        assertEquals(departures.get(42).getId(), 246);
        assertEquals(departures.get(0).getCalendar().name(), Calendar.WEEKDAY.name());
        assertEquals(departures.get(42).getCalendar().name(), Calendar.SATURDAY.name());

    }

    public void testFindStops() throws IOException, JSONException {
        RoutesAPI api = RoutesAPI.getInstance();
        List<Stop> stops = api.findStops(22);

        assertNotNull(stops);
        assertTrue(stops.size() > 0);

        assertEquals(stops.get(0).getId(), 47);
        assertEquals(stops.get(0).getName(), "TICEN");
        assertEquals(stops.get(0).getSequence(), 1);
        assertEquals(stops.get(0).getRouteId(), 22);

    }
}
