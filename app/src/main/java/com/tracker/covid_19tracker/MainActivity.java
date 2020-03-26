package com.tracker.covid_19tracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.tracker.covid_19tracker.client.Client;
import com.tracker.covid_19tracker.files.FileManager;
import com.tracker.covid_19tracker.gui.VisualTracker;
import com.tracker.covid_19tracker.location.LocationTracker;

import java.io.File;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int ANDROID_VERSION = Build.VERSION.SDK_INT;

    // Permission request codes
    private static final int LOCATIONS_REQUEST_CODE = 0;
    private static final int FILE_REQUEST_CODE = 1;

    private LocationTracker locationTracker;
    private VisualTracker visualTracker;
    private FileManager fileManager;
    private Client client;
    private boolean[] permissions = new boolean[10];
    private boolean initialized = false;


    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.d("Debugging", Arrays.toString(getFilesDir().list()));

        this.fileManager = new FileManager(this);
        this.client = new Client(this);
        this.initialized = true;

        initializePermissions();
        client.start();

        Log.d("Debugging", "Starting app");
        Log.d("Debugging", ANDROID_VERSION + "");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initializePermissions(){
        requestPermissions();

        // Will be true if permission is automatically granted (i.e. user not prompted).
        if (permissions[LOCATIONS_REQUEST_CODE]) {
            enableTracking();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions(){
        // Does app have authorization from user to use location services?
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
            case FILE_REQUEST_CODE:
                handleFilePermissions(grantResults);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Debugging", "Paused");
        Log.d("Debugging", "Successfully closed network connections? " + client.stop());
        fileManager.saveAll();
    }

    private void handleLocationPermissions(int[] grantResults){
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            // App is ready to begin tracking location
            Log.d("Debugging", "Location service access granted");
            permissions[LOCATIONS_REQUEST_CODE] = true;
            if (initialized){
                enableTracking();
            }
        } else {
            Log.d("Debugging", "Location service access denied");
            exit();
        }
    }

    private void handleFilePermissions(int[] grantResults){
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            permissions[FILE_REQUEST_CODE] = true;
        } else {
            Log.d("File IO", "File IO access denied");
            exit();
        }
    }

    private void enableTracking(){
        this.visualTracker = findViewById(R.id.visual_tracker);
        this.locationTracker = new LocationTracker(this);
    }

    public void exit(String errMsg){
        if (errMsg != null){
            Log.e("Fatal Error", errMsg);
        }
        exit();
    }

    public void exit(){
        if (client != null){
            Log.d("Debugging", "Successfully closed network connections? " + client.stop());
        }
        if (ANDROID_VERSION >= 21){
            finishAndRemoveTask();
        } else if (ANDROID_VERSION >= 16){
            finishAffinity();
        } else {
            System.exit(0);
        }
    }

    public VisualTracker getVisualTracker() {
        return visualTracker;
    }

    public FileManager getFileManager() {
        return fileManager;
    }
}
