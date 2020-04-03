package com.tracker.covid_19tracker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.tracker.covid_19tracker.MainActivity;
import com.tracker.covid_19tracker.R;
import com.tracker.covid_19tracker.location.LocationEntry;
import com.tracker.covid_19tracker.ui.Infection;
import com.tracker.covid_19tracker.ui.InfectionListAdapter;

import java.util.ArrayList;
import java.util.Random;

public class ContactsFragment extends Fragment {

    private MainActivity mainActivity;
    private View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.root = inflater.inflate(R.layout.contacts_fragment, container, false);
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
