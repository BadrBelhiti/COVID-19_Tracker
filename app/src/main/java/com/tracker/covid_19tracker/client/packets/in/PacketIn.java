package com.tracker.covid_19tracker.client.packets.in;

public abstract class PacketIn {

    protected String raw;

    protected PacketIn(String raw){
        this.raw = raw;
    }

    public int size(){
        return raw.length();
    }

}
