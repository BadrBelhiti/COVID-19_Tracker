package com.tracker.covid_19tracker.files;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.MainActivity;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FileManager {

    private final File MASTER_DIR;

    private MainActivity mainActivity;
    private Set<AbstractFile> files;
    private TrackDataFile trackDataFile;
    private SessionDataFile sessionDataFile;
    private ReportsDataFile reportsDataFile;
    private CachedPacketsFile cachedPacketsFile;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public FileManager(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.MASTER_DIR = mainActivity.getFilesDir();
        this.files = new HashSet<>();
        this.trackDataFile = new TrackDataFile(MASTER_DIR, this);
        this.sessionDataFile = new SessionDataFile(MASTER_DIR, this);
        this.reportsDataFile = new ReportsDataFile(MASTER_DIR, this);
        this.cachedPacketsFile = new CachedPacketsFile(MASTER_DIR, this);

        if (!trackDataFile.load()){
            mainActivity.exit("Failed to load track data from local storage. Closing...");
        }

        if (!sessionDataFile.load()){
            mainActivity.exit("Failed to load session data from local storage. Closing...");
        }

        if (!reportsDataFile.load()){
            mainActivity.exit("Failed to load reports data from local storage. Closing...");
        }

        if (!cachedPacketsFile.load()){
            mainActivity.exit("Failed to load packet data from local storage. Closing...");
        }
    }

    public void saveAll(){
        for (AbstractFile file : files){
            if (!file.save()){
                Log.e("File IO", String.format("Error saving file %s", file.getName()));
            } else {
                Log.d("File IO", String.format("Successfully saved file %s", file.getName()));
            }
        }
    }

    public void registerFile(AbstractFile file){
        files.add(file);
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

    public SessionDataFile getSessionDataFile() {
        return sessionDataFile;
    }

    public ReportsDataFile getReportsDataFile() {
        return reportsDataFile;
    }

    public CachedPacketsFile getCachedPacketsFile() {
        return cachedPacketsFile;
    }
}
