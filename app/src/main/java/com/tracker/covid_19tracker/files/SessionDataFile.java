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

    private static final int REPORT_COOLDOWN = 10 * 1000;

    private UUID userId;
    private long lastReport;
    private long first;
    private long last;
    private boolean symptomatic;

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
        this.symptomatic = false;
        this.lastReport = 0;

        try {
            data.put("uuid", userId.toString());
            data.put("first", first);
            data.put("last", last);
            data.put("symptomatic", symptomatic);
            data.put("last_report", 0);
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
            this.first = data.getLong("first");
            this.last = data.getLong("last");
            this.symptomatic = data.getBoolean("symptomatic");
            this.lastReport = data.getLong("last_report");
        } catch (JSONException e){
            e.printStackTrace();
            return false;
        }

        Log.d("Debugging", data.toString());

        return true;
    }

    public boolean canSubmitReport(){
        return System.currentTimeMillis() - lastReport >= REPORT_COOLDOWN;
    }

    public UUID getUserId() {
        return userId;
    }

    public long getFirst() {
        return first;
    }

    public void setFirst(long first) {
        this.first = first;
        try {
            data.put("first", first);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public long getLast() {
        return last;
    }

    public void setLast(long last) {
        this.last = last;
        try {
            data.put("last", last);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public boolean isSymptomatic() {
        return symptomatic;
    }

    public void setSymptomatic(boolean symptomatic) {
        this.symptomatic = symptomatic;
        this.lastReport = System.currentTimeMillis();

        try {
            data.put("symptomatic", symptomatic);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
