package com.tracker.covid_19tracker.files;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.location.LocationEntry;
import com.tracker.covid_19tracker.location.Track;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TrackDataFile extends AbstractFile {

    private Track track;
    private JSONObject container;

    protected TrackDataFile(File dir, FileManager fileManager) {
        super("track_data.json", dir, fileManager);
        this.track = new Track();
        this.container = new JSONObject();
    }

    public void registerEntry(LocationEntry locationEntry){
        track.addPoint(locationEntry);
        JSONArray location = new JSONArray();

        try {
            location.put(0, locationEntry.getTimestamp());
            location.put(1, locationEntry.getLatitude());
            location.put(2, locationEntry.getLongitude());
            location.put(3, locationEntry.getAltitude());
            container.getJSONArray("location_entries").put(location);
        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void initializeData(){
        this.container = new JSONObject();
        JSONArray locations = new JSONArray();

        try {
            container.put("location_entries", locations);
        } catch (JSONException e){
            e.printStackTrace();
        }

        save();
    }

    @Override
    public void onCreate(boolean successful) {
        super.onCreate(successful);

        if (!successful){
            return;
        }

        Log.d("File IO", "Empty track data. Continuing...");
        initializeData();
        Log.d("File IO", container.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean load() {
        int size;
        byte[] bytes;

        try {
            size = inputStream.available();
            bytes = new byte[size];
            Log.d("File IO", String.format("Read %d bytes of track data", inputStream.read(bytes)));
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }

        String raw = new String(bytes, StandardCharsets.UTF_8);

        Log.d("File IO", file.toPath().toString());
        Log.d("File IO", raw);

        try {
            this.container = new JSONObject(raw);
            JSONArray locations = container.getJSONArray("location_entries");
            int length = locations.length();

            for (int i = 0; i < length; i++){
                JSONArray location = (JSONArray) locations.get(i);
                long timestamp = location.getLong(0);
                double latitude = location.getDouble(1);
                double longitude = location.getDouble(2);
                double altitude = location.getDouble(3);

                LocationEntry locationEntry = new LocationEntry(latitude, longitude, altitude, timestamp);
                track.addPoint(locationEntry);
            }

        } catch (JSONException e){
            e.printStackTrace();
            return false;
        }

        Log.d("Debugging", track.toString());

        return true;
    }

    @Override
    public boolean save() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.append(container.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
