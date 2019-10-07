package com.ashiqur.weatherapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ashiqur.weatherapp.rest_api.ApiClient;
import com.ashiqur.weatherapp.rest_api.models.WeatherDataModel;
import com.ashiqur.weatherapp.rest_api.service.ApiInterface;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiDataRepository {

    private static final String TAG = "ApiDataRepository";
    private MutableLiveData<WeatherDataModel> currentLiveData;
    private WeatherDataModel currentData;

    public ApiDataRepository() {
        currentData = new WeatherDataModel();
        currentLiveData = new MutableLiveData<>();
        fetchRestApiData("90.42","24.17"); //TODO: Get Current Location
    }

    public void fetchRestApiData(String lon, String lat) {

        //NOTE: we dont need to fetch data on AsyncTask since retrofit call.enqueue() does the job asynchronously

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        final Map<String, String> mapdata = new HashMap<>();
        mapdata.put("lat", lat);
        mapdata.put("lon",lon);
        mapdata.put("APPID", "31de1dc4507ce4031564ab9b4a1532f2");
        Call<JsonObject> call = apiInterface.getApiData(mapdata);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    Log.w("WEATHER", response.body().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").toString());
                    Log.w("WEATHER", response.body().get("wind").getAsJsonObject().get("speed").toString());
                    Log.w("WEATHER", response.body().get("main").getAsJsonObject().get("temp").toString());
                    Log.w("WEATHER", response.body().get("clouds").getAsJsonObject().get("all").toString());


                    parseJsonData(response);
                } else {
                    Log.wtf(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //TODO:
                //currentData.setError(true);
                Log.wtf(TAG, "OnFailure:" + t.getMessage());
            }
        });
    }

    public void fetchRestApiDataFromCityName(String cityName, String countryId) {

        //NOTE: we dont need to fetch data on AsyncTask since retrofit call.enqueue() does the job asynchronously

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        final Map<String, String> mapdata = new HashMap<>();
        mapdata.put("q",cityName+","+countryId);
        mapdata.put("APPID", "31de1dc4507ce4031564ab9b4a1532f2");
        Call<JsonObject> call = apiInterface.getApiData(mapdata);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    Log.w("WEATHER", response.body().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").toString());
                    Log.w("WEATHER", response.body().get("wind").getAsJsonObject().get("speed").toString());
                    Log.w("WEATHER", response.body().get("main").getAsJsonObject().get("temp").toString());
                    Log.w("WEATHER", response.body().get("clouds").getAsJsonObject().get("all").toString());


                    parseJsonData(response);
                } else {
                    Log.wtf(TAG, response.message());
                    currentData.setError(true);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //TODO:
                //currentData.setError(true);
                Log.wtf(TAG, "OnFailure:" + t.getMessage());
                currentData.setError(true);
            }
        });
    }

    private void parseJsonData(Response<JsonObject> response) {
        String desc = response.body().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").toString();
        String windSpeed = response.body().get("wind").getAsJsonObject().get("speed").toString();
        String temperature = response.body().get("main").getAsJsonObject().get("temp").toString();
        String clouds = response.body().get("clouds").getAsJsonObject().get("all").toString();
        currentData.setError(false);
        currentData.setClouds(clouds);
        currentData.setDescription(desc);
        currentData.setTemperature(temperature);
        currentData.setWindSpeed(windSpeed);
        currentLiveData.setValue(currentData);

    }


    public MutableLiveData<WeatherDataModel> getCurrentLiveData() {
        return currentLiveData;
    }
}
