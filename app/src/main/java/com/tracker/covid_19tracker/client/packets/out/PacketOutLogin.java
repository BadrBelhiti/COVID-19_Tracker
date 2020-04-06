package com.tracker.covid_19tracker.client.packets.out;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.util.UUID;

public class PacketOutLogin extends PacketOut {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PacketOutLogin(UUID uuid) {
        super(0, uuid);
        packData(null);
    }
}
