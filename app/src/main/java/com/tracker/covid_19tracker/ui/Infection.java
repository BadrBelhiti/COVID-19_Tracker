package com.tracker.covid_19tracker.ui;

import com.tracker.covid_19tracker.location.LocationEntry;

public class Infection {

    private LocationEntry contact;
    private boolean isActive;

    public Infection(LocationEntry contact, boolean isActive){
        this.contact = contact;
        this.isActive = isActive;
    }

    public Infection(LocationEntry contact){
        this(contact, true);
    }

    public LocationEntry getContact() {
        return contact;
    }

    public boolean isActive() {
        return isActive;
    }
}
