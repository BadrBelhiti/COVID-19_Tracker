package com.tracker.covid_19tracker.files;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.Utils;
import com.tracker.covid_19tracker.location.LocationEntry;
import com.tracker.covid_19tracker.location.Track;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class TrackDataFile extends AbstractFile {

    private Track track;

    protected TrackDataFile(File dir, FileManager fileManager) {
        super("track_data.json", dir, fileManager);
        this.track = new Track();
    }

    public void registerEntry(LocationEntry locationEntry){
        track.addPoint(locationEntry);

        try {
            data.getJSONArray("location_entries").put(Utils.entryToArray(locationEntry));
        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate(boolean successful) {
        super.onCreate(successful);

        if (!successful){
            mainActivity.exit("Failed to create track_data.json file. Closing...");
            return;
        }

        try {
            data.put("location_entries", new JSONArray());
        } catch (JSONException e){
            e.printStackTrace();
        }

        save();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean load() {
        String raw = Utils.fromStream(inputStream);

        if (raw == null){
            return false;
        }

        try {
            this.data = new JSONObject(raw);
            this.track = Utils.getTrack(raw);
        } catch (JSONException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public Track getTrack() {
        return track;
    }
}
