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

public class ReportsDataFile extends AbstractFile {

    private List<Infection> infections;

    protected ReportsDataFile(File dir, FileManager fileManager) {
        super("reports.json", dir, fileManager);
        this.infections = new ArrayList<>();
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

    public List<Infection> getInfections() {
        return infections;
    }
}
