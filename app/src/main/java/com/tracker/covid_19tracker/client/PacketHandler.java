package com.tracker.covid_19tracker.client;

import android.util.Log;
import com.tracker.covid_19tracker.MainActivity;
import com.tracker.covid_19tracker.client.packets.in.PacketInInfection;
import com.tracker.covid_19tracker.location.LocationEntry;
import com.tracker.covid_19tracker.location.Track;
import com.tracker.covid_19tracker.ui.Infection;
import org.json.JSONException;
import org.json.JSONObject;

public class PacketHandler {

    private MainActivity mainActivity;

    public PacketHandler(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void handlePacket(String raw) {
        JSONObject data;
        int id;
        JSONObject payload;

        try {
            data = new JSONObject(raw);
            id = data.getInt("id");
            payload = data.getJSONObject("data");
        } catch (JSONException e){
            e.printStackTrace();
            Log.e("Client Error", "Error parsing packet");
            return;
        }

        Log.d("Debugging", id + "");

        if (id == 3){
            PacketInInfection packetInInfection = new PacketInInfection(payload.toString());
            Track infectionTrack = packetInInfection.getTrack();
            Track myTrack = mainActivity.getFileManager().getTrackDataFile().getTrack();
            myTrack.addPoint(new LocationEntry(0, 0, 0, 0));

            LocationEntry contact = myTrack.getLastContact(infectionTrack);

            if (contact == null){
                Log.d("Debugging", "No paths crossed with infected user!");
            } else {
                Log.d("Debugging", "Uh oh... " + contact.toString());
                mainActivity.getFileManager().getReportsDataFile().add(new Infection(contact, true), false);
            }
        }

    }

}
