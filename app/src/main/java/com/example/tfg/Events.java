package com.example.tfg;

import android.media.Image;

public class Events {
    String EVENT,DATE,MONTHS,YEAR;
    //String IMAGEN;

    public Events(String EVENT, String DATE, String MONTHS, String YEAR) {
        this.EVENT = EVENT;
        this.DATE = DATE;
        this.MONTHS = MONTHS;
        this.YEAR = YEAR;
        //this.IMAGEN = IMAGEN;
    }

   /* public String getIMAGEN() {
        return IMAGEN;
    }

    public void setIMAGEN(String IMAGEN) {
        this.IMAGEN = IMAGEN;
    }
*/
    public String getEVENT() {
        return EVENT;
    }

    public void setEVENT(String EVENT) {
        this.EVENT = EVENT;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getMONTHS() {
        return MONTHS;
    }

    public void setMONTHS(String MONTHS) {
        this.MONTHS = MONTHS;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }
}
