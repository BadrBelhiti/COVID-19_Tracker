package com.tracker.covid_19tracker.client.packets.out;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import org.json.JSONException;
import org.json.JSONObject;

public class PacketOutInfection extends PacketOut {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected PacketOutInfection() {
        super(1);

        JSONObject payload = new JSONObject();

        try {
            payload.put("data", payload);
        } catch (JSONException e){
            e.printStackTrace();
            Log.e("Client Error", "Error building PacketOutInfection");
        }

        packData(payload);
    }
}
