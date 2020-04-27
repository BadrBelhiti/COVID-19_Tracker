package com.tracker.covid_19tracker.ui;

import com.tracker.covid_19tracker.R;

import java.util.ArrayList;
import java.util.List;

public enum Symptom {

    COUGH("Frequent Coughing", R.id.cough, 0),
    SNEEZE("Frequent Sneezing", R.id.sneeze, 1),
    FEVER("Fever", R.id.fever, 2),
    BREATH("Shortness of Breath", R.id.breath, 3),
    NOSE("Runny Nose", R.id.nose, 4),
    ACHES("Body Aches", R.id.aches, 5),
    FATIGUE("Fatigue", R.id.fatigue, 6),
    THROAT("Sore Throat", R.id.throat, 7);

    String name;
    int id;
    int pos;

    Symptom(String name, int id, int pos){
        this.name = name;
        this.id = id;
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getPos() {
        return pos;
    }

    public static List<Symptom> getSymptoms(int bits){
        List<Symptom> symptoms = new ArrayList<>();

        for (Symptom symptom : values()){
            if (((bits >> symptom.pos) & 1) == 1){
                symptoms.add(symptom);
            }
        }

        return symptoms;
    }

    public static int getSymptomsAsInt(List<Symptom> symptoms){
        int res = 0;
        for (Symptom symptom : symptoms){
            res |= (1 << symptom.pos);
        }
        return res;
    }

}
