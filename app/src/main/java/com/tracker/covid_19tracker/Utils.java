package com.tracker.covid_19tracker;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.location.LocationEntry;
import com.tracker.covid_19tracker.location.Track;
import com.tracker.covid_19tracker.ui.Infection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.TreeSet;

public class Utils {

    // Takes in JSON string
    public static Track getTrack(String data) throws JSONException {
        return getTrack(new JSONObject(data));
    }

    public static Track getTrack(JSONObject data) throws JSONException {
        return getTrack(data.getJSONArray("location_entries"));
    }

    public static Track getTrack(JSONArray locations) throws JSONException {
        Track track = new Track();

        int length = locations.length();

        for (int i = 0; i < length; i++){
            JSONArray location = (JSONArray) locations.get(i);
            track.addPoint(arrayToEntry(location));
        }

        return track;
    }

    public static Infection getReport(JSONObject report){
        LocationEntry locationEntry = null;
        boolean isActive = false;

        try {
            locationEntry = arrayToEntry(report.getJSONArray("location"));
            isActive = report.getBoolean("active");
        } catch (JSONException e){
            e.printStackTrace();
        }

        return new Infection(locationEntry, isActive);
    }

    public static JSONArray reportsToArray(TreeSet<Infection> infections){
        JSONArray jsonArray = new JSONArray();

        for (Infection infection : infections){
            jsonArray.put(reportToObject(infection));
        }

        return jsonArray;
    }

    public static JSONObject reportToObject(Infection infection){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("location", entryToArray(infection.getContact()));
            jsonObject.put("active", infection.isActive());
        } catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObject;
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

    public static LocationEntry arrayToEntry(String data) throws JSONException {
        return arrayToEntry(new JSONArray(data));
    }

    public static LocationEntry arrayToEntry(JSONArray jsonArray) throws JSONException{
        long timestamp = jsonArray.getLong(0);
        double latitude = jsonArray.getDouble(1);
        double longitude = jsonArray.getDouble(2);
        double altitude = jsonArray.getDouble(3);

        return new LocationEntry(latitude, longitude, altitude, timestamp);
    }

    public static JSONArray trackToArray(Track track){

        TreeSet<LocationEntry> locationEntries = track.getSet();
        JSONArray jsonArray = new JSONArray();

        for (LocationEntry locationEntry : locationEntries){
            jsonArray.put(entryToArray(locationEntry));
        }

        return jsonArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String fromStream(InputStream inputStream){
        int size;
        byte[] bytes;

        try {
            size = inputStream.available();
            bytes = new byte[size];
            Log.d("File IO", String.format("Read %d bytes of track data", inputStream.read(bytes)));
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String zeroPad(String str, int length){
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < length){
            sb.insert(0, "0");
        }
        return sb.toString();
    }

}
