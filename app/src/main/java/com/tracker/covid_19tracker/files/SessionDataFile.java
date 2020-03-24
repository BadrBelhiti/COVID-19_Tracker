package com.tracker.covid_19tracker.files;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.UUID;

public class SessionDataFile extends AbstractFile {

    private UUID userId;
    private long first;
    private long last;

    protected SessionDataFile(File dir, FileManager fileManager) {
        super("session_data.json", dir, fileManager);
    }

    @Override
    public void onCreate(boolean successful) {
        super.onCreate(successful);

        if (!successful){
            mainActivity.exit("Failed to create track_data.json file. Closing...");
            return;
        }

        Log.d("Debugging", "Creating session data...");

        this.userId = UUID.randomUUID();
        this.first = 0;
        this.last = 0;

        try {
            data.put("uuid", userId.toString());
            data.put("first", first);
            data.put("last", last);
        } catch (JSONException e){
            e.printStackTrace();
        }

        save();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean load() {
        String raw = Utils.fromStream(inputStream);

        if (raw == null){
            return false;
        }

        try {
            this.data = new JSONObject(raw);
            this.userId = UUID.fromString(data.getString("uuid"));
            this.first = data.getInt("first");
            this.last = data.getInt("last");
        } catch (JSONException e){
            e.printStackTrace();
            return false;
        }

        Log.d("Debugging", data.toString());

        return true;
    }
}
