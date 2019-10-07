package com.ashiqur.weatherapp.rest_api.service;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    @GET("weather")
    Call<JsonObject> getApiData(@QueryMap Map<String, String> options);
}
