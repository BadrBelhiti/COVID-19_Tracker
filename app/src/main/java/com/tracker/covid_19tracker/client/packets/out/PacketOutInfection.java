package com.tracker.covid_19tracker.client.packets.out;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.Utils;
import com.tracker.covid_19tracker.location.Track;
import com.tracker.covid_19tracker.ui.Infection;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.TreeSet;
import java.util.UUID;

public class PacketOutInfection extends PacketOut {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PacketOutInfection(UUID uuid, Track track, TreeSet<Infection> reports) {
        super(2, uuid);

        JSONObject payload = new JSONObject();

        try {
            payload.put("track", Utils.trackToArray(track));
            payload.put("previous_contacts", Utils.reportsToArray(reports));
        } catch (JSONException e){
            e.printStackTrace();
            Log.e("Client Error", "Error building PacketOutInfection");
        }

        packData(payload);
    }
}
