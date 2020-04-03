package com.tracker.covid_19tracker.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.tracker.covid_19tracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class InfectionListAdapter extends ArrayAdapter<Infection> {

    private Activity context;
    private ArrayList<Infection> entries;
    private DateFormat dateFormat;

    public InfectionListAdapter(Activity context, ArrayList<Infection> entries) {
        super(context, R.layout.infection, entries);
        this.context = context;
        this.entries = entries;
        this.dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Infection getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View entry = convertView;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null){
            entry = inflater.inflate(R.layout.infection, null, true);
        }

        TextView main = entry.findViewById(R.id.main_text);
        TextView subtext = entry.findViewById(R.id.subtext);
        ImageView icon = entry.findViewById(R.id.warning_icon);

        Infection infection = entries.get(position);


        String mainText = infection.isActive() ? "Active Symptoms" : "Recovered";
        String subtextText = "Last contact: " + dateFormat.format(new Date(infection.getContact().getTimestamp()));
        int res = infection.isActive() ? R.drawable.ic_caution : R.drawable.ic_checkmark;

        main.setText(mainText);
        subtext.setText(subtextText);
        icon.setImageResource(res);

        return entry;
    }
}
