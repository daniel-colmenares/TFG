package com.example.tfg.remote;

public class APIUtils {

    private APIUtils(){
    };

    public static final String API_URL = "https://api.arasaac.org/api/pictograms/";

    public static PictogramService getUserService(){
        return RetrofitClient.getClient(API_URL).create(PictogramService.class);
    }

}