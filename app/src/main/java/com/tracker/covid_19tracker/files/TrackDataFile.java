package com.tracker.covid_19tracker.files;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.Utils;
import com.tracker.covid_19tracker.location.LocationEntry;
import com.tracker.covid_19tracker.location.Track;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class TrackDataFile extends AbstractFile {

    private Track track;

    protected TrackDataFile(File dir, FileManager fileManager) {
        super("track_data_1.json", dir, fileManager);
        this.track = new Track();
    }

    public void registerEntry(LocationEntry locationEntry){
        track.addPoint(locationEntry);
    }

    @Override
    public void onCreate(boolean successful) {
        super.onCreate(successful);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean load() {
        int size = 0;
        byte[] bytes = null;
        try {
            size = inputStream.available();
            bytes = new byte[size];
            inputStream.read(bytes);
        } catch (IOException e){
            e.printStackTrace();
        }


        assert bytes != null;
        String raw = new String(bytes, StandardCharsets.UTF_8);;

        Log.d("File IO", file.toPath().toString());
        Log.d("File IO", raw);

        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(raw);
        } catch (JSONException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean save() {
        return false;
    }
}
