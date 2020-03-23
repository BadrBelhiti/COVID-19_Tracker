package com.tracker.covid_19tracker.files;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileManager {

    private final File MASTER_DIR;

    private MainActivity mainActivity;
    private TrackDataFile trackDataFile;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public FileManager(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.MASTER_DIR = mainActivity.getFilesDir();
        this.trackDataFile = new TrackDataFile(MASTER_DIR, this);

        try {
            FileInputStream inputStream = mainActivity.getApplicationContext().openFileInput("track_data_1.json");
            FileOutputStream outputStream = mainActivity.getApplicationContext().openFileOutput("track_data_1.json", Activity.MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ///*
        if (!trackDataFile.load()){
            Log.e("File IO", "Failed to load track data from local storage. Closing...");
            mainActivity.exit();
        }
        // */
    }

    public void saveAll(){
        if (!trackDataFile.save()){
            Log.e("File I/O", "Error saving track data");
        }
    }

    public File getMASTER_DIR() {
        return MASTER_DIR;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public TrackDataFile getTrackDataFile() {
        return trackDataFile;
    }
}
