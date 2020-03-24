package com.tracker.covid_19tracker.client.packets.out;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public abstract class PacketOut {

    protected JSONObject payload;
    protected int id;
    protected byte[] data;

    protected PacketOut(int id){
        this.id = id;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void packData(JSONObject payload){
        this.payload = payload;

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("data", payload);
            this.data = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
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
}
