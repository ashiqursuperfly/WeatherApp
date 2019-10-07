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
    public static final int FROM_CITY_NAME = 0;
    public static final int FROM_LAT_LON = 1;
    private static final String TAG = "ApiDataRepository";
    private MutableLiveData<WeatherDataModel> currentLiveData;
    private WeatherDataModel currentData;

    public ApiDataRepository() {
        currentData = new WeatherDataModel();
        currentLiveData = new MutableLiveData<>();
        fetchRestApiCurrentWeatherData("90.42","24.17",ApiDataRepository.FROM_LAT_LON); //TODO: Get Current Location
    }

    public void fetchRestApiCurrentWeatherData(String param1, String param2,int MODE) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        final Map<String, String> mapdata = new HashMap<>();


        if(MODE == FROM_LAT_LON) {
            mapdata.put("lat", param2);
            mapdata.put("lon", param1);
        }
        else if(MODE == FROM_CITY_NAME){
            mapdata.put("q",param1+","+param2);
        }
        else{
            return;
        }
        mapdata.put("APPID", "31de1dc4507ce4031564ab9b4a1532f2");

        Call<JsonObject> call = apiInterface.getCurrentWeatherApiData(mapdata);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    Log.w("WEATHER", response.body().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").toString());
                    Log.w("WEATHER", response.body().get("wind").getAsJsonObject().get("speed").toString());
                    Log.w("WEATHER", response.body().get("main").getAsJsonObject().get("temp").toString());
                    Log.w("WEATHER", response.body().get("clouds").getAsJsonObject().get("all").toString());


                    parseCurrentWeatherJson(response);
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



    private void parseCurrentWeatherJson(Response<JsonObject> response) {
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
