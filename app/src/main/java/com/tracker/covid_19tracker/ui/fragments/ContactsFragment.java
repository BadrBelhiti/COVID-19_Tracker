package com.tracker.covid_19tracker.ui.fragments;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import com.tracker.covid_19tracker.MainActivity;
import com.tracker.covid_19tracker.R;
import com.tracker.covid_19tracker.location.LocationEntry;
import com.tracker.covid_19tracker.ui.Infection;
import com.tracker.covid_19tracker.ui.InfectionListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContactsFragment extends Fragment {

    private MainActivity mainActivity;
    private View root;
    private ArrayList<Infection> infectionsList = new ArrayList<>();
    private InfectionListAdapter infections;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.infections = new InfectionListAdapter(mainActivity, infectionsList);
        Log.d("Debugging", "Initialized list");
        ListView listView = root.findViewById(R.id.list);
        listView.setAdapter(infections);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.root = inflater.inflate(R.layout.contacts_fragment, container, false);
    }

    public void addInfection(Infection infection){
        infectionsList.add(infection);

        Intent intent = new Intent(mainActivity, MainActivity.class);
        intent.putExtra("fragment", "contacts");

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mainActivity, MainActivity.getNotificationsName()).
                setSmallIcon(R.drawable.ic_caution).
                setContentTitle("Symptoms Reported Nearby").
                setContentText("Click to view details").
                setPriority(NotificationCompat.PRIORITY_DEFAULT).
                setContentIntent(PendingIntent.getActivity(mainActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        mainActivity.showNotification(builder, (int) (infection.getContact().getTimestamp() % Integer.MAX_VALUE));
    }

    private void initList(){
        Random random = new Random();
        ArrayList<Infection> infections = new ArrayList<>();

        for (int i = 0; i < 10; i++){
            LocationEntry locationEntry = new LocationEntry(0, 0, 0, random.nextLong());
            Infection infection = new Infection(locationEntry);
            infections.add(infection);
        }

        InfectionListAdapter adapter = new InfectionListAdapter(mainActivity, infections);

        ListView listView = root.findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    public static ContactsFragment newInstance(MainActivity mainActivity) {
        ContactsFragment fragment = new ContactsFragment();
        fragment.mainActivity = mainActivity;
        return fragment;
    }

}
