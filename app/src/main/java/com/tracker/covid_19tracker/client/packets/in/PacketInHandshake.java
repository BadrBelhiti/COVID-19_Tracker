package com.tracker.covid_19tracker.client.packets.in;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.MainActivity;
import com.tracker.covid_19tracker.client.packets.out.PacketOutLogin;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class PacketInHandshake extends PacketIn {

    private UUID sessionId;

    public PacketInHandshake(String raw){
        super(raw);
        try {
            JSONObject payload = new JSONObject(raw);
            this.sessionId = UUID.fromString(payload.getString("key"));
        } catch (JSONException e){
            e.printStackTrace();
            Log.e("Error", "Failed to parse authentication packet");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void handle(MainActivity mainActivity) {
        mainActivity.getClient().setSessionID(sessionId);

        // Echo back key
        mainActivity.getClient().send(new PacketOutLogin());
    }
}
