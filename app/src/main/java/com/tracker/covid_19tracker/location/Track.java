package com.tracker.covid_19tracker.location;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Track {

    private static final int TRANSMISSION_RADIUS_METERS = 2;
    private static final int TIME_WINDOW_MILLIS = 1000 * 60 * 5;

    private TreeSet<LocationEntry> track;

    public Track(){
        this.track = new TreeSet<>();
    }

    public void addPoint(LocationEntry locationEntry){
        track.add(locationEntry);
    }

    public LocationEntry getLastContact(Track other){
        List<LocationEntry> list = new ArrayList<>(this.track);
        List<LocationEntry> otherList = new ArrayList<>(other.track);

        if (list.size() == 0 || otherList.size() == 0){
            return null;
        }

        int a = list.size() - 1;
        int b = otherList.size() - 1;

        long aTime, bTime;

        while (a >= 0 && b >= 0 &&
                Math.abs((aTime = list.get(a).getTimestamp()) - (bTime = otherList.get(b).getTimestamp())) > TIME_WINDOW_MILLIS &&
                list.get(a).distanceSquared(otherList.get(b)) > TRANSMISSION_RADIUS_METERS * TRANSMISSION_RADIUS_METERS){
            if (aTime > bTime){
                a--;
            } else {
                b--;
            }
        }

        return Math.min(a, b) < 0 ? null : list.get(a);
    }

}
