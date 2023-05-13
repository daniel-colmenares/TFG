package com.example.tfg.remote;

import com.example.tfg.model.Pictograma;

import org.json.JSONArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PictogramService {

    @GET("all/es/")
    Call<List<JSONArray>> getPictos();

    @GET("{id}")
    Call<Pictograma> getOne(@Path("id") int id);

}
