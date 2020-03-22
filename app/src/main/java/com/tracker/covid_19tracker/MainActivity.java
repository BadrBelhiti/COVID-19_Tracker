package com.tracker.covid_19tracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.tracker.covid_19tracker.location.LocationTracker;

public class MainActivity extends AppCompatActivity {

    private static final int ANDROID_VERSION = Build.VERSION.SDK_INT;

    // Permission request codes
    private static final int LOCATIONS_REQUEST_CODE = 0;

    private LocationTracker locationTracker;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        requestPermissions();

        Log.d("Debugging", "Starting app");
        Log.d("Debugging", ANDROID_VERSION + "");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermissions(){
        // Does app have authorization from user?
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permissions not granted by default. Prompt user.
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATIONS_REQUEST_CODE);
        } else {
            // Permissions already granted beforehand.
            handleLocationPermissions(new int[]{PackageManager.PERMISSION_GRANTED});
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATIONS_REQUEST_CODE:
                handleLocationPermissions(grantResults);
                break;
        }
    }

    private void handleLocationPermissions(int[] grantResults){
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            // App is ready to begin tracking location
            Log.d("Debugging", "Location service access granted");
            this.locationTracker = new LocationTracker(this);
        } else {
            // TODO: Close app
            Log.d("Debugging", "Location service access denied");
        }
    }

}
