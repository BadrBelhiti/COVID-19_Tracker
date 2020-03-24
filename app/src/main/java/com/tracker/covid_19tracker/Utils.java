package com.tracker.covid_19tracker;

import com.tracker.covid_19tracker.location.LocationEntry;
import com.tracker.covid_19tracker.location.Track;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.TreeSet;

public class Utils {

    public static byte[] getBytes(File file){
        FileInputStream fileInputStream;
        byte[] bytes = new byte[(int) file.length()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            fileInputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        return bytes;
    }

    public static byte[] getBytes(FileInputStream fileInputStream, int size){
        byte[] bytes = new byte[size];
        try {
            fileInputStream.read(bytes);
            fileInputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return bytes;
    }

    // Takes in JSON string
    public static Track getTrack(String data) throws JSONException {
        return getTrack(new JSONObject(data));
    }

    public static Track getTrack(JSONObject data) throws JSONException {
        Track track = new Track();

        JSONArray locations = data.getJSONArray("location_entries");
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

        return track;
    }

    public static JSONArray entryToArray(LocationEntry locationEntry){
        JSONArray jsonArray = new JSONArray();

        try {
            jsonArray.put(0, locationEntry.getTimestamp());
            jsonArray.put(1, locationEntry.getLatitude());
            jsonArray.put(2, locationEntry.getLongitude());
            jsonArray.put(3, locationEntry.getAltitude());
        } catch (JSONException e){
            e.printStackTrace();
        }

        return jsonArray;
    }

    public static JSONArray trackToArray(Track track){

        TreeSet<LocationEntry> locationEntries = track.getSet();
        JSONArray jsonArray = new JSONArray();

        for (LocationEntry locationEntry : locationEntries){
            jsonArray.put(entryToArray(locationEntry));
        }

        return jsonArray;
    }

}
