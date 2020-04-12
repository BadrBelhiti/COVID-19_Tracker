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
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    protected PacketOut(int id, UUID uuid){
        super(new JSONObject());
        this.id = id;
        this.uuid = uuid;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void packData(JSONObject payload){
        this.payload = payload;

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("uuid", uuid.toString());
            if (payload != null){
                jsonObject.put("data", payload);
            }
            String packet = jsonObject.toString();
            this.data = (Utils.zeroPad(packet.length() + "", 8) + packet).getBytes(StandardCharsets.UTF_8);
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
