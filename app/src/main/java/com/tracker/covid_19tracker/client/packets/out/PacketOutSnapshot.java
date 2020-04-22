package com.tracker.covid_19tracker.client.packets.out;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.Utils;
import com.tracker.covid_19tracker.location.LocationEntry;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class PacketOutSnapshot extends PacketOut {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PacketOutSnapshot(UUID uuid, UUID sessionID, LocationEntry locationEntry, long first, long last) {
        super(1, uuid, sessionID);

        JSONObject payload = new JSONObject();

        try {
            payload.put("location", Utils.entryToArray(locationEntry));
            payload.put("first", first);
            payload.put("last", last);
        } catch (JSONException e){
            e.printStackTrace();
            Log.e("Client Error", "Error building PacketOutSnapshot");
        }

        packData(payload);
    }
}
