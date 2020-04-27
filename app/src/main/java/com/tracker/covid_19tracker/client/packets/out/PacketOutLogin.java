package com.tracker.covid_19tracker.client.packets.out;

import android.os.Build;
import androidx.annotation.RequiresApi;

public class PacketOutLogin extends PacketOut {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PacketOutLogin() {
        super(0);
        packData(null);
    }
}
