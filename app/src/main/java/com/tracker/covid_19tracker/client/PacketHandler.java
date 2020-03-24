package com.tracker.covid_19tracker.client;

import com.tracker.covid_19tracker.MainActivity;
import com.tracker.covid_19tracker.client.packets.in.PacketInInfection;
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
            Track track = packetInInfection.getTrack();
            // TODO: Run calculations and alert user if needed
        }

    }

}
