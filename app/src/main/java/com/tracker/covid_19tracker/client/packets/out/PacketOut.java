package com.tracker.covid_19tracker.client.packets.out;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public abstract class PacketOut {

    protected JSONObject payload;
    protected int id;
    protected byte[] data;

    protected PacketOut(int id){
        this.id = id;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void packData(JSONObject payload, UUID uuid){
        this.payload = payload;

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("uuid", uuid.toString());
            jsonObject.put("data", payload);
            String packet = jsonObject.toString();
            this.data = (Utils.zeroPad(packet.length() + "", 8) + packet).getBytes(StandardCharsets.UTF_8);
            Log.d("Debugging", new String(data));
        } catch (JSONException e){
            e.printStackTrace();
            Log.e("Client Error", "Error building packet");
        }

    }

    public int size(){
        return data.length;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return new String(data);
    }
}
