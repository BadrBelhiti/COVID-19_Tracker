package com.tracker.covid_19tracker.ui;

import java.util.ArrayList;
import java.util.List;

public enum Symptom {

    COUGH("Frequent Coughing", "cough", 0),
    SNEEZE("Frequent Sneezing", "sneeze", 1),
    FEVER("Fever", "fever", 2),
    BREATH("Shortness of Breath", "breathe", 3),
    NOSE("Runny Nose", "nose", 4),
    ACHES("Body Aches", "aches", 5),
    FATIGUE("Fatigue", "fatigue", 6),
    THROAT("Sore Throat", "throat", 7);

    String name;
    String id;
    int pos;

    Symptom(String name, String id, int pos){
        this.name = name;
        this.id = id;
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public String getId() {
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

}
