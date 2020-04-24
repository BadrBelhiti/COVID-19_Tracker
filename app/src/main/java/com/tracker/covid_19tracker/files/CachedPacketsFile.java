package com.tracker.covid_19tracker.files;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.Utils;
import com.tracker.covid_19tracker.client.packets.out.PacketOut;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

public class CachedPacketsFile extends AbstractFile {

    private Queue<PacketOut> cached;

    public CachedPacketsFile(File dir, FileManager fileManager) {
        super("cached_packets.json", dir, fileManager);
        this.cached = new LinkedList<>();
    }

    @Override
    public void onCreate(boolean successful) {
        super.onCreate(successful);

        if (!successful){
            mainActivity.exit("Failed to create cached_packets.json file. Closing...");
            return;
        }

        try {
            data.put("packets", new JSONArray());
        } catch (JSONException e){
            e.printStackTrace();
        }

        save();
    }

    public void cache(PacketOut packetOut){
        cached.add(packetOut);

        try {
            ((JSONArray) data.get("packets")).put(packetOut.toString());
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean load() {

        String raw = Utils.fromStream(inputStream);

        Log.d("Debugging", raw == null ? "Null" : raw);

        if (raw == null){
            return false;
        }

        try {
            this.data = new JSONObject(raw);

            JSONArray packets = (JSONArray) data.get("packets");
            int size = packets.length();

            for (int i = 0; i < size; i++){
                String packetData = packets.getString(i);
                PacketOut packet = new PacketOut(packetData.getBytes(StandardCharsets.UTF_8));
                cached.add(packet);
            }

        } catch (JSONException e){
            e.printStackTrace();
        }

        return true;
    }

    public Queue<PacketOut> getCached() {
        return cached;
    }
}
