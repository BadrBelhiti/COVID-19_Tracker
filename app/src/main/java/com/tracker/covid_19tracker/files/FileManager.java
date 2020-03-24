package com.tracker.covid_19tracker.files;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.MainActivity;

import java.io.File;

public class FileManager {

    private final File MASTER_DIR;

    private MainActivity mainActivity;
    private TrackDataFile trackDataFile;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public FileManager(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.MASTER_DIR = mainActivity.getFilesDir();
        this.trackDataFile = new TrackDataFile(MASTER_DIR, this);

        if (!trackDataFile.load()){
            Log.e("File IO", "Failed to load track data from local storage. Closing...");
            mainActivity.exit();
        }
    }

    public void saveAll(){
        if (!trackDataFile.save()){
            Log.e("File IO", "Error saving track data");
        } else {
            Log.d("File IO", "Successfully saved all files");
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
