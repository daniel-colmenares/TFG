package com.example.tfg;

public class Calendars {
    String NAME;
    String EMAIL;

    public Calendars(String NAME, String EMAIL) {
        this.NAME = NAME;
        this.EMAIL = EMAIL;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}
