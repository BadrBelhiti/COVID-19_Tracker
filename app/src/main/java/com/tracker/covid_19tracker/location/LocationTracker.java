package com.tracker.covid_19tracker.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.tracker.covid_19tracker.MainActivity;

import java.util.TreeMap;

public class LocationTracker implements LocationListener {

    private static final int MIN_TIME_MILLIS = 1000 * 5;
    private static final int MIN_DISTANCE_METERS = 10;

    private MainActivity mainActivity;
    private LocationManager locationManager;

    // Sort location data in chronological order
    private TreeMap<Long, LocationEntry> entries;

    @SuppressLint("MissingPermission")
    public LocationTracker(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
        this.entries = new TreeMap<>();

        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_MILLIS, MIN_DISTANCE_METERS, this);
        } else {
            Log.e("Location Tracking", "Fatal error initializing location services. Closing...");
            // TODO: Close app
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        LocationEntry locationEntry = new LocationEntry(location);
        entries.put(System.currentTimeMillis(), locationEntry);

        Log.d("Debugging", String.format("Latitude: %f, Longitude: %f%n", lat, lon));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
