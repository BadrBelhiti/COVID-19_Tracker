package com.tracker.covid_19tracker.client.packets.out;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.Utils;
import com.tracker.covid_19tracker.client.packets.Packet;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PacketOut extends Packet {

    protected JSONObject payload;
    protected int id;
    protected byte[] data;

    public PacketOut(byte[] data){
        super(new String(data));
        this.data = data;
    }

    public PacketOut(String raw) throws JSONException {
        this(new JSONObject(raw));
    }

    public PacketOut(JSONObject data){
        super(data);
        try {
            this.payload = data.getJSONObject("data");
            this.id = data.getInt("id");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    protected PacketOut(int id){
        super(new JSONObject());
        this.id = id;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void packData(JSONObject payload){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            if (payload != null){
                jsonObject.put("data", payload);
            }
            this.payload = jsonObject;
        } catch (JSONException e){
            e.printStackTrace();
            Log.e("Client Error", "Error building packet");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void finalizeToSend(UUID userId, UUID sessionID){
        try {
            payload.put("uuid", userId.toString());
            if (!(this instanceof PacketOutLogin)){
                payload.put("session_id", sessionID.toString());
            }
            String packet = payload.toString();
            this.data = (Utils.zeroPad(packet.length() + "", 8) + packet).getBytes(StandardCharsets.UTF_8);
        } catch (JSONException e){
            e.printStackTrace();
            Log.e("Client Error", "Error finalizing packet");
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
        return payload.toString();
    }
}
