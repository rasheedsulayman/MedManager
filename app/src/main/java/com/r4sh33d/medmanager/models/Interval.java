package com.r4sh33d.medmanager.models;

public class Interval {
    private String title;
    private   long timeInMilliseconds;

    public Interval(String title, double hours) {
        this.title = title;
        this.timeInMilliseconds = convertHoursToMilliseconds(hours);
    }

    private long convertHoursToMilliseconds(double hours){
        return (long) hours*60*60*1000;
    }

    public long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
