package com.tracker.covid_19tracker.ui;

import com.tracker.covid_19tracker.location.LocationEntry;

public class Infection implements Comparable<Infection> {

    private LocationEntry contact;
    private boolean isActive;

    public Infection(LocationEntry contact, boolean isActive){
        this.contact = contact;
        this.isActive = isActive;
    }

    public Infection(LocationEntry contact){
        this(contact, true);
    }

    @Override
    public int compareTo(Infection o) {
        return contact.compareTo(o.contact);
    }

    public LocationEntry getContact() {
        return contact;
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
