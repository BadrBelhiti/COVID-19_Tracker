package com.tracker.covid_19tracker.client.packets.in;

import android.util.Log;
import com.tracker.covid_19tracker.Utils;
import com.tracker.covid_19tracker.location.Track;
import org.json.JSONException;

public class PacketInInfection extends PacketIn {

    private Track track;

    public PacketInInfection(String data){
        super(data);

        try {
            this.track = Utils.getTrack(data);
        } catch (JSONException e){
            e.printStackTrace();
            Log.e("Error", "Failed to parse infection packet");
        }
    }

    public Track getTrack() {
        return track;
    }
}
