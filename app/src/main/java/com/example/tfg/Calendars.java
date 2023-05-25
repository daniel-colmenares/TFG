package com.example.tfg;

import java.util.Date;

public class Calendars {
    String NAME;
    String EMAIL;
    String FECHA, COLOR;
    Integer ID, LETRA;

    public Calendars(String NAME, String EMAIL,String FECHA, Integer ID, String COLOR, Integer LETRA) {
        this.NAME = NAME;
        this.EMAIL = EMAIL;
        this.FECHA = FECHA;
        this.ID= ID;
        this.COLOR=COLOR;
        this.LETRA=LETRA;
    }


    public Integer getLETRA() {
        return LETRA;
    }

    public void setLETRA(Integer LETRA) {
        this.LETRA = LETRA;
    }

    public String getCOLOR() {
        return COLOR;
    }

    public void setCOLOR(String COLOR) {
        this.COLOR = COLOR;
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
