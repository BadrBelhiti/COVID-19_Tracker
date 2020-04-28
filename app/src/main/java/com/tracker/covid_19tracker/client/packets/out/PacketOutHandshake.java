package com.tracker.covid_19tracker.client.packets.out;

import android.os.Build;
import androidx.annotation.RequiresApi;

public class PacketOutHandshake extends PacketOut {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PacketOutHandshake(){
        super(0);
        packData(null);
    }
}
