package com.example.tfg.remote;

public class APIUtils {

    private APIUtils(){
    };

    public static final String API_URL = "http://169.254.35.189:8080/demo/";

    public static UserService getUserService(){
        return RetrofitClient.getClient(API_URL).create(UserService.class);
    }

}