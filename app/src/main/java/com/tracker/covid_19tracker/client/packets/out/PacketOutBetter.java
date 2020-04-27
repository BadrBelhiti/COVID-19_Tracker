package com.tracker.covid_19tracker.client.packets.out;

import android.os.Build;
import androidx.annotation.RequiresApi;

public class PacketOutBetter extends PacketOut {


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PacketOutBetter() {
        super(4);
        packData(null);
    }
}
