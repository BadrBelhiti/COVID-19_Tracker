package com.tracker.covid_19tracker.location;

import android.location.Location;

public class LocationEntry implements Comparable<LocationEntry> {

    private double latitude;
    private double longitude;
    private long timestamp;

    public LocationEntry(Location location){
        this(location.getLatitude(), location.getLongitude(), System.currentTimeMillis());
    }

    public LocationEntry(double latitude, double longitude, long timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(LocationEntry o) {
        // Avoiding using (int) Long.compare(...) for compatibility reasons

        long diff = this.timestamp - o.timestamp;
        if (diff > 0){
            return 1;
        } else if (diff < 0){
            return -1;
        }

        return 0;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
