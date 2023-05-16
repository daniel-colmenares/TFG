package com.example.tfg.remote;

import com.google.gson.annotations.SerializedName;

public class Keyword {
    @SerializedName("keyword")
    private String keyword;
    @SerializedName("type")
    private Integer type;
    @SerializedName("meaning")
    private String meaning;
    @SerializedName("plural")
    private String plural;
    @SerializedName("hasLocution")
    private Boolean hasLocution;

    public Keyword(String keyword, Integer type, String meaning, String plural, Boolean hasLocution) {
        this.keyword = keyword;
        this.type = type;
        this.meaning = meaning;
        this.plural = plural;
        this.hasLocution = hasLocution;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    public Boolean getHasLocution() {
        return hasLocution;
    }

    public void setHasLocution(Boolean hasLocution) {
        this.hasLocution = hasLocution;
    }
}
