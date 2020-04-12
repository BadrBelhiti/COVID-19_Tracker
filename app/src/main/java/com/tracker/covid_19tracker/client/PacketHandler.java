package com.tracker.covid_19tracker.client;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.MainActivity;
import com.tracker.covid_19tracker.client.packets.Packet;
import com.tracker.covid_19tracker.client.packets.Packets;
import com.tracker.covid_19tracker.client.packets.in.PacketIn;
import org.json.JSONException;
import org.json.JSONObject;

public class PacketHandler {

    private MainActivity mainActivity;

    public PacketHandler(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void handlePacket(String raw) {
        JSONObject data;
        int id;
        JSONObject payload;

        try {
            data = new JSONObject(raw);
            id = data.getInt("id");
            payload = data.getJSONObject("data");
        } catch (JSONException e){
            e.printStackTrace();
            Log.e("Client Error", "Error parsing packet");
            return;
        }

        Log.d("Debugging", id + "");

        Packet packet = Packets.getPacket(id, payload.toString());
        if (!(packet instanceof PacketIn)){
            Log.d("Debugging", "Received bad packet");
            return;
        }

        PacketIn packetIn = (PacketIn) packet;
        packetIn.handle(mainActivity);
    }

}
