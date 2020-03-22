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

    public LocationEntry add(LocationEntry locationEntry){
        return new LocationEntry(this.latitude + locationEntry.latitude, this.longitude + locationEntry.longitude, -1);
    }

    public LocationEntry subtract(LocationEntry locationEntry){
        return new LocationEntry(this.latitude - locationEntry.latitude, this.longitude - locationEntry.longitude, -1);
    }

    // Using Euclidean geometry is fine as we'll be dealing with small distances.
    public double distanceSquared(LocationEntry locationEntry){
        return Math.pow(this.latitude - locationEntry.latitude, 2) + Math.pow(this.longitude - locationEntry.longitude, 2);
    }

    public double distance(LocationEntry locationEntry){
        return Math.sqrt(distanceSquared(locationEntry));
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

    @Override
    public String toString() {
        return "LocationEntry{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", timestamp=" + timestamp +
                '}';
    }
}
