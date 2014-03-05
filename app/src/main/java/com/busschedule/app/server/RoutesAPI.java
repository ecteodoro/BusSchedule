package com.busschedule.app.server;

import android.util.JsonReader;

import com.busschedule.app.model.Calendar;
import com.busschedule.app.model.Departure;
import com.busschedule.app.model.Route;
import com.busschedule.app.model.Stop;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is responsible for making HTTP requests
 * to the feed server.
 * It is also responsible for parsing JSON feed and return data
 * as a list of model objects.
 */
public class RoutesAPI {

    public static final String STOP_NAME = "stopName";
    public static final String ROWS = "rows";
    public static final String ID = "id";
    public static final String SHORT_NAME = "shortName";
    public static final String LONG_NAME = "longName";
    public static final String ROUTE_ID = "routeId";
    public static final String CALENDAR = "calendar";
    public static final String TIME = "time";
    public static final String NAME = "name";
    public static final String SEQUENCE = "sequence";

    private static final String URL = "https://dashboard.appglu.com/v1/queries/";
    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH = "Basic V0tENE43WU1BMXVpTThWOkR0ZFR0ek1MUWxBMGhrMkMxWWk1cEx5VklsQVE2OA==";
    private static final String ENV_HEADER = "X-AppGlu-Environment";
    private static final String ENV = "staging";
    private static final String CONTENT_HEADER = "Content-Type";
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private static final String ROUTE_RESOURCE_URI = "findRoutesByStopName/run";
    private static final String DEPARTURE_RESOURCE_URI = "findDeparturesByRouteId/run";
    private static final String STOP_RESOURCE_URI = "findStopsByRouteId/run";
    private static final String PARAMS = "params";
    private static final String ENCODING = "UTF-8";

    private static RoutesAPI instance;

    private RoutesAPI() {
    }

    public static RoutesAPI getInstance() {
        if (instance == null)
            instance = new RoutesAPI();
        return instance;
    }

    //TODO there must be some space for refactoring the next 3 methods

    //TODO a 3rd party JSON parsing library would probably be more suitable

    public List<Route> findRoutes(String stopName) throws JSONException, IOException {
        StringEntity params = getParams(STOP_NAME, "%" + stopName.toLowerCase() + "%");
        String uri = URL + ROUTE_RESOURCE_URI;
        JsonReader reader = getJsonFeed(uri, params);
        List<Route> routes = new ArrayList<Route>();
        reader.beginObject();
        while (reader.hasNext()) {
            if (reader.nextName().equals(ROWS)) {
                reader.beginArray();
                while (reader.hasNext()) {
                    int id = -1;
                    String shortName = null;
                    String longName = null;
                    reader.beginObject();
                    String key;
                    while (reader.hasNext()) {
                        key = reader.nextName();
                        if (key != null) {
                            if (key.equals(ID))
                                id = reader.nextInt();
                            else if (key.equals(SHORT_NAME))
                                shortName = reader.nextString();
                            else if (key.equals(LONG_NAME))
                                longName = reader.nextString();
                            else
                                reader.skipValue();
                        }
                    }
                    reader.endObject();
                    Route route = new Route(id, shortName, longName);
                    routes.add(route);
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        Collections.sort(routes);
        return routes;
    }

    public List<Departure> findDepartures(int routeId) throws JSONException, IOException {
        StringEntity params = getParams(ROUTE_ID, Integer.toString(routeId));
        String uri = URL + DEPARTURE_RESOURCE_URI;
        JsonReader reader = getJsonFeed(uri, params);
        List<Departure> departures = new ArrayList<Departure>();
        reader.beginObject();
        while (reader.hasNext()) {
            if (reader.nextName().equals(ROWS)) {
                reader.beginArray();
                while (reader.hasNext()) {
                    int id = -1;
                    Calendar calendar = null;
                    String time = null;
                    reader.beginObject();
                    String key;
                    while (reader.hasNext()) {
                        key = reader.nextName();
                        if (key != null) {
                            if (key.equals(ID))
                                id = reader.nextInt();
                            else if (key.equals(CALENDAR))
                                calendar = Calendar.valueOf(reader.nextString());
                            else if (key.equals(TIME))
                                time = reader.nextString();
                            else
                                reader.skipValue();
                        }
                    }
                    reader.endObject();
                    Departure departure = new Departure(id, calendar, time);
                    departures.add(departure);
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        Collections.sort(departures);
        return departures;
    }

    public List<Stop> findStops(int routeId) throws JSONException, IOException {
        StringEntity params = getParams(ROUTE_ID, Integer.toString(routeId));
        String uri = URL + STOP_RESOURCE_URI;
        JsonReader reader = getJsonFeed(uri, params);
        List<Stop> stops = new ArrayList<Stop>();
        reader.beginObject();
        while (reader.hasNext()) {
            if (reader.nextName().equals(ROWS)) {
                reader.beginArray();
                while (reader.hasNext()) {
                    int id = -1;
                    String name = null;
                    int sequence = -1;
                    int route = -1;
                    reader.beginObject();
                    String key;
                    while (reader.hasNext()) {
                        key = reader.nextName();
                        if (key != null) {
                            if (key.equals(ID))
                                id = reader.nextInt();
                            else if (key.equals(NAME))
                                name = reader.nextString();
                            else if (key.equals(SEQUENCE))
                                sequence = reader.nextInt();
                            else if (key.equals(ROUTE_ID))
                                route = reader.nextInt();
                            else
                                reader.skipValue();
                        }
                    }
                    reader.endObject();
                    Stop stop = new Stop(id, route, name, sequence);
                    stops.add(stop);
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        Collections.sort(stops);
        return stops;
    }

    /*
    Make HTTP request and retrive data as a JsonReader object
    that must be parsed.
     */
    private JsonReader getJsonFeed(String uri, StringEntity params) throws IOException {
        HttpPost request = new HttpPost(uri);
        request.addHeader(AUTH_HEADER, AUTH);
        request.addHeader(ENV_HEADER, ENV);
        request.addHeader(CONTENT_HEADER, CONTENT_TYPE);
        request.setEntity(params);
        HttpResponse response = new DefaultHttpClient().execute(request);
        return new JsonReader(new InputStreamReader(response.getEntity().getContent(), ENCODING));
    }

    /*
    Prepares the POST request parameters, according to the desired feed.
     */
    private StringEntity getParams(String paramKey, String paramValue) throws JSONException, UnsupportedEncodingException {
        JSONObject params = new JSONObject();
        params.put(PARAMS, new JSONObject().put(paramKey, paramValue));
        return new StringEntity(params.toString(), ENCODING);
    }

}
