package com.tracker.covid_19tracker.files;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.tracker.covid_19tracker.Utils;
import com.tracker.covid_19tracker.ui.Infection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ReportsDataFile extends AbstractFile {

    private Set<Infection> infections;

    protected ReportsDataFile(File dir, FileManager fileManager) {
        super("reports.json", dir, fileManager);
        this.infections = new TreeSet<>();
    }

    @Override
    public void onCreate(boolean successful) {
        super.onCreate(successful);

        if (!successful){
            mainActivity.exit("Failed to create reports.json file. Closing...");
            return;
        }

        try {
            this.data.put("reports", new JSONArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        save();
    }

    public void add(Infection infection, boolean init){
        infections.add(infection);
        mainActivity.getContactsFragment().addInfection(infection);

        if (!init) {
            try {
                ((JSONArray) data.get("reports")).put(Utils.reportToObject(infection));
                Log.d("Debugging", "##" + data.get("reports"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

            JSONArray reports = data.getJSONArray("reports");
            int size = reports.length();

            for (int i = 0; i < size; i++){
                Infection infection = Utils.getReport((JSONObject) reports.get(i));
                add(infection, true);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public TreeSet<Infection> getLastReports(long timeMillis){
        long currentTime = System.currentTimeMillis();
        TreeSet<Infection> res = new TreeSet<>();

        // 'infections' is already sorted by time
        for (Infection infection : infections){
            if ((currentTime - infection.getContact().getTimestamp()) <= timeMillis){
                res.add(infection);
            } else {
                break;
            }
        }

        return res;
    }

    public Set<Infection> getInfections() {
        return infections;
    }
}
