package com.tracker.covid_19tracker.location;

import android.location.Location;

public class LocationEntry implements Comparable<LocationEntry> {

    private static final double DEGREES_TO_METERS = 111_319.488;

    private double latitude;
    private double longitude;
    private double altitude;
    private long timestamp;

    public LocationEntry(Location location){
        this(location.getLatitude(), location.getLongitude(), location.getAltitude(), System.currentTimeMillis());
    }

    public LocationEntry(double latitude, double longitude, double altitude, long timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(LocationEntry o) {
        return Long.compare(timestamp, o.timestamp);
    }

    public LocationEntry add(LocationEntry locationEntry){
        return new LocationEntry(this.latitude + locationEntry.latitude, this.longitude + locationEntry.longitude, this.altitude + locationEntry.altitude, -1);
    }

    public LocationEntry subtract(LocationEntry locationEntry){
        return new LocationEntry(this.latitude - locationEntry.latitude, this.longitude - locationEntry.longitude, this.altitude + locationEntry.altitude, -1);
    }

    public LocationEntry multiply(double scalar){
        return new LocationEntry(scalar * latitude, scalar * longitude, scalar * altitude, -1);
    }

    public LocationEntry divide(double divisor){
        return multiply(1 / divisor);
    }

    // Using Euclidean geometry is fine as we'll be dealing with small distances. Altitude is ignored for now.
    public double distanceSquared(LocationEntry locationEntry){
        return Math.pow(DEGREES_TO_METERS, 2) * Math.pow(this.latitude - locationEntry.latitude, 2) + Math.pow((this.longitude - locationEntry.longitude) * Math.cos(Math.toRadians(latitude)), 2);
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

    public double getAltitude() {
        return altitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "LocationEntry{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", timestamp=" + timestamp +
                '}';
    }
}
