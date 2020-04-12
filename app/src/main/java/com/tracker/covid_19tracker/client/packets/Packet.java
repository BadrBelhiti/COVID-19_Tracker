package com.tracker.covid_19tracker.client.packets;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Packet {

    protected JSONObject data;

    protected Packet(JSONObject data){
        this.data = data;
    }

    protected Packet(String data){
        try {
            this.data = new JSONObject(data);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

}
