package com.tracker.covid_19tracker.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.tracker.covid_19tracker.MainActivity;
import com.tracker.covid_19tracker.R;
import com.tracker.covid_19tracker.client.packets.out.PacketOut;
import com.tracker.covid_19tracker.client.packets.out.PacketOutBetter;
import com.tracker.covid_19tracker.client.packets.out.PacketOutInfection;
import com.tracker.covid_19tracker.files.SessionDataFile;


public class ReportFragment extends Fragment {

    private MainActivity mainActivity;
    private View root;
    private SessionDataFile sessionDataFile;

    private boolean symptomatic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button report = mainActivity.findViewById(R.id.report);

        report.setOnClickListener((e) -> {
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
        this.symptomatic = sessionDataFile.isSymptomatic();
    }

    public void setSymptomatic(boolean symptomatic){

        if (!sessionDataFile.canSubmitReport()){
            Log.d("Debugging", "You have submitted a report already recently");
            return;
        }


        this.symptomatic = symptomatic;
        mainActivity.getFileManager().getSessionDataFile().setSymptomatic(symptomatic);

        Button button = mainActivity.findViewById(R.id.report);

        if (!symptomatic){
            button.setText(R.string.report_button);
            button.setTextColor(R.color.colorPrimaryDark);
            mainActivity.getClient().send(new PacketOutBetter(sessionDataFile.getUserId()));
        } else {
            button.setText(R.string.better_button);
            button.setTextColor(R.color.green);
            mainActivity.getClient().send(new PacketOutInfection(sessionDataFile.getUserId(), mainActivity.getFileManager().getTrackDataFile().getTrack()));
        }
    }

    public static ReportFragment newInstance(MainActivity mainActivity) {
        ReportFragment fragment = new ReportFragment();
        fragment.mainActivity = mainActivity;
        return fragment;
    }
    
}
