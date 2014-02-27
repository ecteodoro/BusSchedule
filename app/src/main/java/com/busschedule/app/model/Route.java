package com.busschedule.app.model;

public class Route implements Comparable<Route>{

    private int id;
    private String shortName;
    private String longName;

    public Route(int id, String shortName, String longName) {
        this.setId(id);
        this.setShortName(shortName);
        this.setLongName(longName);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    @Override
    public int compareTo(Route route) {
        return Integer.valueOf(this.getShortName()) - Integer.valueOf(route.getShortName());
    }
}
