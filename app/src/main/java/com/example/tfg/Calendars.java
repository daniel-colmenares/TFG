package com.example.tfg;

public class Calendars {
    String NAME;
    String EMAIL;
    String FECHA;
    Integer ID;

    public Calendars(String NAME, String EMAIL,String FECHA, Integer ID) {
        this.NAME = NAME;
        this.EMAIL = EMAIL;
        this.FECHA = FECHA;
        this.ID= ID;
    }

    public String getFECHA() {
        return FECHA;
    }

    public void setFECHA(String FECHA) {
        this.FECHA = FECHA;
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

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }
}
