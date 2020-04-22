package com.tracker.covid_19tracker.client.packets.out;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.util.UUID;

public class PacketOutBetter extends PacketOut {


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PacketOutBetter(UUID uuid, UUID sessionID) {
        super(4, uuid, sessionID);
        packData(null);
    }
}
