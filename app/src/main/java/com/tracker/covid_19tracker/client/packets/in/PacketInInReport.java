package com.tracker.covid_19tracker.client.packets.in;

import android.util.Log;
import com.tracker.covid_19tracker.MainActivity;
import com.tracker.covid_19tracker.Utils;
import com.tracker.covid_19tracker.location.LocationEntry;
import com.tracker.covid_19tracker.location.Track;
import com.tracker.covid_19tracker.ui.Infection;
import com.tracker.covid_19tracker.ui.Symptom;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class PacketInInReport extends PacketIn {

    private UUID uuid;
    private Track track;
    private int symptoms;

    public PacketInInReport(String data){
        super(data);

        try {
            JSONObject payload = new JSONObject(data);
            this.uuid = UUID.fromString(payload.getString("uuid"));
            this.track = Utils.getTrack(payload.getJSONArray("location_entries"));
            this.symptoms = payload.getInt("symptoms");
        } catch (JSONException e){
            e.printStackTrace();
            Log.e("Error", "Failed to parse infection packet");
        }
    }

    @Override
    public void handle(MainActivity mainActivity) {
        Track myTrack = mainActivity.getFileManager().getTrackDataFile().getTrack();
        LocationEntry contact = myTrack.getLastContact(track);

        if (contact == null){
            Log.d("Debugging", "No paths crossed with infected user!");
        } else {
            Log.d("Debugging", "Close Contact Reported: " + contact.toString());
            mainActivity.getFileManager().getReportsDataFile().add(new Infection(contact, Symptom.getSymptoms(symptoms), true), false);
        }
    }

    public Track getTrack() {
        return track;
    }
}
