package com.example.tfg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pictograma {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("keyword")
    @Expose
    private String keyword;

    public Pictograma() {
    }

    public Pictograma(int id, String keyword) {
        this.id = id;
        this.keyword = keyword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword= keyword;
    }
}
