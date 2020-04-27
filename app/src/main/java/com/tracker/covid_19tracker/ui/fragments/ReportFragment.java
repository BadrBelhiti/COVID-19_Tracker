package com.tracker.covid_19tracker.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.tracker.covid_19tracker.MainActivity;
import com.tracker.covid_19tracker.R;
import com.tracker.covid_19tracker.client.packets.out.PacketOutBetter;
import com.tracker.covid_19tracker.client.packets.out.PacketOutInfection;
import com.tracker.covid_19tracker.files.ReportsDataFile;
import com.tracker.covid_19tracker.files.SessionDataFile;
import com.tracker.covid_19tracker.files.TrackDataFile;
import com.tracker.covid_19tracker.ui.Infection;
import com.tracker.covid_19tracker.ui.Symptom;


public class ReportFragment extends Fragment {

    private static final int MAX_INCUBATION = 14 * 24 * 60 * 60 * 1000;

    private MainActivity mainActivity;
    private View root;
    private SessionDataFile sessionDataFile;
    private ReportsDataFile reportsDataFile;
    private TrackDataFile trackDataFile;
    private Button button;

    private boolean symptomatic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.button = mainActivity.findViewById(R.id.report);

        button.setOnClickListener((e) -> {
            Log.d("Debugging", "Report clicked");
            setSymptomatic(!symptomatic);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.root = inflater.inflate(R.layout.report_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.sessionDataFile = mainActivity.getFileManager().getSessionDataFile();
        this.reportsDataFile = mainActivity.getFileManager().getReportsDataFile();
        this.trackDataFile = mainActivity.getFileManager().getTrackDataFile();
        this.symptomatic = sessionDataFile.isSymptomatic();
    }

    public void setSymptomatic(boolean symptomatic){

        if (!sessionDataFile.canSubmitReport()){
            Log.d("Debugging", "You have submitted a report already recently");
            return;
        }


        this.symptomatic = symptomatic;
        mainActivity.getFileManager().getSessionDataFile().setSymptomatic(symptomatic);

        if (!symptomatic){
            button.setText(R.string.report_button);
            button.setTextColor(R.color.colorPrimaryDark);
            mainActivity.getClient().send(new PacketOutBetter(sessionDataFile.getUserId(), mainActivity.getClient().getSessionID()));
        } else {

            // TODO: Show symptom selector

            int symptoms = 0;

            for (Symptom symptom : Symptom.values()){
                CheckBox checkBox = mainActivity.findViewById(symptom.getId());
                if (checkBox.isChecked()){
                    symptoms |= (1 << symptom.getPos());
                }
            }

            button.setText(R.string.better_button);
            button.setTextColor(R.color.green);
            PacketOutInfection packet = new PacketOutInfection(sessionDataFile.getUserId(), mainActivity.getClient().getSessionID(), trackDataFile.getTrack(), Symptom.getSymptoms(symptoms), reportsDataFile.getLastReports(MAX_INCUBATION));
            mainActivity.getClient().send(packet);
        }
    }

    public static ReportFragment newInstance(MainActivity mainActivity) {
        ReportFragment fragment = new ReportFragment();
        fragment.mainActivity = mainActivity;
        return fragment;
    }
    
}
