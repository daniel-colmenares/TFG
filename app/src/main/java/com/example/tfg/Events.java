package com.example.tfg;

import android.media.Image;
import android.net.Uri;

public class Events {
    String EVENT,DATE,MONTHS,YEAR, VIDEO;
    Uri IMAGEN;
    //String IMAGEN;

    public Events(String EVENT, String DATE, String MONTHS, String YEAR, Uri IMAGEN, String VIDEO) {
        this.EVENT = EVENT;
        this.DATE = DATE;
        this.MONTHS = MONTHS;
        this.YEAR = YEAR;
        this.IMAGEN = IMAGEN;
    }

    public String getVIDEO() {
        return VIDEO;
    }

    public void setVIDEO(String VIDEO) {
        this.VIDEO = VIDEO;
    }

    public Uri getIMAGEN() {
        return IMAGEN;
    }

    public void setIMAGEN(Uri IMAGEN) {
        this.IMAGEN = IMAGEN;
    }

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
