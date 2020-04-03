package com.tracker.covid_19tracker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.tracker.covid_19tracker.MainActivity;
import com.tracker.covid_19tracker.R;

public class LocalCasesFragment extends Fragment {

    private MainActivity mainActivity;
    private View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.root = inflater.inflate(R.layout.cases_fragment, container, false);
    }

    public static LocalCasesFragment newInstance(MainActivity mainActivity) {
        LocalCasesFragment fragment = new LocalCasesFragment();
        fragment.mainActivity = mainActivity;
        return fragment;
    }

}
