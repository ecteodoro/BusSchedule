package com.busschedule.app.model;

public class Departure implements Comparable<Departure> {

    private int id;
    private Calendar calendar;
    private String time;

    public Departure(int id, Calendar calendar, String time) {
        this.setId(id);
        this.setCalendar(calendar);
        this.setTime(time);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int compareTo(Departure departure) {
        // compare day of the week
        int compare = this.getCalendar().compareTo(departure.getCalendar());
        if (compare == 0) // if day of the week is the equal then compare time
            compare = this.getTime().compareTo(departure.getTime());
        return compare;
    }
}
