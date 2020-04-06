package com.tracker.covid_19tracker.client.packets.out;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.Utils;
import com.tracker.covid_19tracker.location.Track;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class PacketOutInfection extends PacketOut {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PacketOutInfection(UUID uuid, Track track) {
        super(2, uuid);

        JSONObject payload = new JSONObject();

        try {
            payload.put("track", Utils.trackToArray(track));
        } catch (JSONException e){
            e.printStackTrace();
            Log.e("Client Error", "Error building PacketOutInfection");
        }

        packData(payload);
    }
}
