package com.example.tfg.remote;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class Modelo {

    @SerializedName("_id")
    private Integer id;
    @SerializedName("keywords")
    private List<Keyword> keywords;


    public Modelo(Integer id, List<Keyword> keywords) {
        this.id = id;
        this.keywords = keywords;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }
}

