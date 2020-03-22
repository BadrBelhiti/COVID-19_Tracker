package com.tracker.covid_19tracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.tracker.covid_19tracker.gui.VisualTracker;
import com.tracker.covid_19tracker.location.LocationTracker;

public class MainActivity extends AppCompatActivity {

    private static final int ANDROID_VERSION = Build.VERSION.SDK_INT;

    // Permission request codes
    private static final int LOCATIONS_REQUEST_CODE = 0;

    private LocationTracker locationTracker;
    private VisualTracker visualTracker;
    private boolean[] permissions = new boolean[10];

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        requestPermissions();

        // Will be true if permission is automatically granted (i.e. user not prompted).
        if (permissions[LOCATIONS_REQUEST_CODE]) {
            this.visualTracker = findViewById(R.id.visual_tracker);
            enableTracking();
        }

        Log.d("Debugging", (visualTracker == null) + "");


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
            permissions[LOCATIONS_REQUEST_CODE] = true;
            if (visualTracker != null){
                enableTracking();
            }
        } else {
            // TODO: Close app
            Log.d("Debugging", "Location service access denied");
        }
    }

    private void enableTracking(){
        this.locationTracker = new LocationTracker(this);
    }

    public VisualTracker getVisualTracker() {
        return visualTracker;
    }
}
