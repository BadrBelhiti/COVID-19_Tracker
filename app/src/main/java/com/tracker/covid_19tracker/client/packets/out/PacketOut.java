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
    private UUID uuid;
    private UUID sessionID;
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
            this.uuid = UUID.fromString(data.getString("uuid"));
            this.id = data.getInt("id");

            if (!(this instanceof PacketOutLogin)){
                this.sessionID = UUID.fromString(data.getString("session_id"));
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    protected PacketOut(int id, UUID uuid, UUID sessionID){
        super(new JSONObject());
        this.id = id;
        this.uuid = uuid;
        this.sessionID = sessionID;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void packData(JSONObject payload){
        this.payload = payload;

        try {
            JSONObject jsonObject = new JSONObject();
            if (payload != null){
                jsonObject.put("data", payload);
            }
        } catch (JSONException e){
            e.printStackTrace();
            Log.e("Client Error", "Error building packet");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void finalizeToSend(UUID userId, UUID sessionID){
        try {
            payload.put("id", id);
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
