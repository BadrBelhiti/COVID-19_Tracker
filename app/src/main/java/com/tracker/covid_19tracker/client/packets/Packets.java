package com.tracker.covid_19tracker.client.packets;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.client.packets.in.PacketInInfection;
import com.tracker.covid_19tracker.client.packets.in.PacketInLogin;
import com.tracker.covid_19tracker.client.packets.out.PacketOutBetter;
import com.tracker.covid_19tracker.client.packets.out.PacketOutInfection;
import com.tracker.covid_19tracker.client.packets.out.PacketOutLogin;
import com.tracker.covid_19tracker.client.packets.out.PacketOutSnapshot;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum Packets {

    OUT_LOGIN(PacketOutLogin.class, 0), OUT_SNAPSHOT(PacketOutSnapshot.class, 1),
    OUT_INFECTION(PacketOutInfection.class, 2), IN_INFECTION(PacketInInfection.class, 3),
    OUT_BETTER(PacketOutBetter.class, 4), IN_LOGIN(PacketInLogin.class, 5);

    Class<? extends Packet> packetClass;
    int id;

    Packets(Class<? extends Packet> packetClass, int id){
        this.packetClass = packetClass;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Packet getPacket(int id, String payload){
        Class<? extends Packet> packetClass = null;
        for (Packets packet : Packets.values()){
            if (packet.getId() == id){
                packetClass = packet.packetClass;
                break;
            }
        }

        if (packetClass == null){
            return null;
        }

        Packet packet = null;

        try {
            Constructor<? extends Packet> constructor = packetClass.getConstructor(String.class);
            packet = constructor.newInstance(payload);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }

        return packet;
    }
}
