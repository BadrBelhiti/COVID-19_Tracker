package com.tracker.covid_19tracker.ui;

import com.tracker.covid_19tracker.location.LocationEntry;

import java.util.ArrayList;
import java.util.List;

public class Infection implements Comparable<Infection> {

    private LocationEntry contact;
    private List<Symptom> symptoms;
    private boolean isActive;

    public Infection(LocationEntry contact, List<Symptom> symptoms, boolean isActive){
        this.contact = contact;
        this.symptoms = symptoms;
        this.isActive = isActive;
    }

    public Infection(LocationEntry contact){
        this(contact, new ArrayList<>(), true);
    }

    @Override
    public int compareTo(Infection o) {
        return contact.compareTo(o.contact);
    }

    public int getSymptomsAsInt(){
        int res = 0;
        for (Symptom symptom : symptoms){
            res |= (1 << symptom.pos);
        }
        return res;
    }

    public LocationEntry getContact() {
        return contact;
    }

    public List<Symptom> getSymptoms() {
        return symptoms;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "Infection{" +
                "contact=" + contact +
                ", isActive=" + isActive +
                '}';
    }
}
