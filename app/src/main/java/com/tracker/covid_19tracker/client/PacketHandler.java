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

    public void handlePacket(String raw) throws JSONException {
        JSONObject data = new JSONObject(raw);
        int id = data.getInt("id");
        JSONObject payload = data.getJSONObject("data");

        Log.d("Debugging", id + "");

        if (id == 3){
            PacketInInfection packetInInfection = new PacketInInfection(payload.toString());
            Track infectionTrack = packetInInfection.getTrack();
            Track myTack = mainActivity.getFileManager().getTrackDataFile().getTrack();
            LocationEntry contact = myTack.getLastContact(infectionTrack);

            if (contact == null){
                Log.d("Debugging", "No paths crossed with infected user!");
            } else {
                Log.d("Debugging", "Uh oh... " + contact.toString());
                mainActivity.getContactsFragment().addInfection(new Infection(contact));
            }
        }

    }

}
