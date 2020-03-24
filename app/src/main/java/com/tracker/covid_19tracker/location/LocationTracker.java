package com.tracker.covid_19tracker.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.tracker.covid_19tracker.MainActivity;
import com.tracker.covid_19tracker.files.TrackDataFile;
import com.tracker.covid_19tracker.gui.VisualTracker;

public class LocationTracker implements LocationListener {

    private static final int MIN_TIME_MILLIS = 1000 * 5;
    private static final int MIN_DISTANCE_METERS = 1;
    private static final double MIN_VEHICLE_SPEED_MPS = 5;
    private static final double SCALE = 2e5;

    private MainActivity mainActivity;
    private LocationManager locationManager;
    private VisualTracker visualTracker;
    private TrackDataFile trackDataFile;
    private LocationEntry lastEntry;

    private double speed = 0;

    @SuppressLint("MissingPermission")
    public LocationTracker(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.visualTracker = mainActivity.getVisualTracker();
        this.locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
        this.trackDataFile = mainActivity.getFileManager().getTrackDataFile();

        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_MILLIS, MIN_DISTANCE_METERS, this);
        } else {
            Log.e("Location Tracking", "Fatal error initializing location services. Closing...");
            mainActivity.exit();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.speed = location.getSpeed();

        // Only log location if not in vehicle (i.e. traveling less than 5 meters/second).
        if (!isRidingVehicle()) {
            LocationEntry locationEntry = new LocationEntry(location);
            trackDataFile.registerEntry(locationEntry);

            LocationEntry diff;
            if (lastEntry == null){
                diff = new LocationEntry(0, 0, 0, -1);
            } else {
                diff = locationEntry.subtract(lastEntry);
            }

            // Move cursor relative to last location and draw new point
            visualTracker.moveCursor(diff.getLatitude() * SCALE, diff.getLongitude() * SCALE);
            visualTracker.invalidate();

            // Update last location
            this.lastEntry = locationEntry;
        }

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

    public boolean isRidingVehicle(){
        return this.speed >= MIN_VEHICLE_SPEED_MPS;
    }
}
