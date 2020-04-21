package com.tracker.covid_19tracker.client.packets.in;

import android.util.Log;
import com.tracker.covid_19tracker.MainActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class PacketInLogin extends PacketIn {

    private UUID sessionID;

    public PacketInLogin(String raw) {
        super(raw);
        try {
            JSONObject payload = new JSONObject(raw);
            this.sessionID = UUID.fromString(payload.getString("key"));
        } catch (JSONException e){
            e.printStackTrace();
            Log.e("Error", "Failed to parse authentication packet");
        }
    }

    @Override
    public void handle(MainActivity mainActivity) {
        mainActivity.getClient().setSessionID(sessionID);
    }
}
