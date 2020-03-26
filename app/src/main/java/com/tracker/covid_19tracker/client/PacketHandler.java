package com.tracker.covid_19tracker.client;

import android.util.Log;
import com.tracker.covid_19tracker.MainActivity;
import com.tracker.covid_19tracker.client.packets.in.PacketInInfection;
import com.tracker.covid_19tracker.location.LocationEntry;
import com.tracker.covid_19tracker.location.Track;
import org.json.JSONException;
import org.json.JSONObject;

public class PacketHandler {

    private MainActivity mainActivity;

    public PacketHandler(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }


    /*
    0: Client -> Server (location snapshot)
    1: Client -> Server (infection)
    2: Server -> Client (infection)
     */

    public void handlePacket(String raw) throws JSONException {
        JSONObject data = new JSONObject(raw);
        int id = data.getInt("id");
        JSONObject payload = data.getJSONObject("data");

        if (id == 2){
            PacketInInfection packetInInfection = new PacketInInfection(payload.toString());
            Track infectionTrack = packetInInfection.getTrack();
            Track myTack = mainActivity.getFileManager().getTrackDataFile().getTrack();
            LocationEntry contact = myTack.getLastContact(infectionTrack);

            if (contact == null){
                Log.d("Debugging", "No paths crossed with infected user!");
            } else {
                Log.d("Debugging", "Uh oh... " + contact.toString());
            }
        }

    }

}
