package com.example.tfg.remote;

import com.example.tfg.model.Pictograma;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PictogramService {

    @Headers("Accept: application/json")
    @GET("all/es/")
    Call<List<Modelo>> getPictos();

    @Headers("Accept: application/json")
    @GET("{id}")
    Call<Pictograma> getOne(@Path("id") int id);

    @Headers("Accept: application/json")
    @GET("es/search/{filtro}")
    Call<List<Modelo>> getByFilter(@Path("filtro") String filtro);

}
