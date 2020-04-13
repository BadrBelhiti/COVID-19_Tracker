package com.tracker.covid_19tracker.client.packets.in;

import com.tracker.covid_19tracker.MainActivity;
import com.tracker.covid_19tracker.client.packets.Packet;

public abstract class PacketIn extends Packet {

    public PacketIn(String raw){
        super(raw);
    }

    public int size(){
        return data.length();
    }

    public abstract void handle(MainActivity mainActivity);

}
