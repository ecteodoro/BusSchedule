package com.busschedule.app.model;

public class Stop implements Comparable<Stop> {

    private int id;
    private int routeId;
    private String name;
    private int sequence;

    public Stop(int id, int routeId, String name, int sequence) {
        this.setId(id);
        this.setRouteId(routeId);
        this.setName(name);
        this.setSequence(sequence);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Override
    public int compareTo(Stop stop) {
        return this.getSequence() - stop.getSequence();
    }

}
