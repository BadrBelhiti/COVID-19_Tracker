package com.tracker.covid_19tracker.client.packets;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.client.packets.in.PacketInHandshake;
import com.tracker.covid_19tracker.client.packets.in.PacketInInReport;
import com.tracker.covid_19tracker.client.packets.out.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum Packets {

    OUT_HANDSHAKE(PacketOutHandshake.class, 0),
    IN_HANDSHAKE(PacketInHandshake.class, 1),
    OUT_KEEPALIVE(PacketOutKeepAlive.class, 2),
    OUT_LOGIN(PacketOutLogin.class, 3),
    OUT_SNAPSHOT(PacketOutSnapshot.class, 4),
    OUT_INFECTION(PacketOutReport.class, 5),
    IN_INFECTION(PacketInInReport.class, 6),
    OUT_BETTER(PacketOutBetter.class, 7);

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
