package com.ashiqur.weatherapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ashiqur.weatherapp.rest_api.ApiClient;
import com.ashiqur.weatherapp.rest_api.models.WeatherDataModel;
import com.ashiqur.weatherapp.rest_api.service.ApiInterface;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ApiDataRepository {
    public static final int FROM_CITY_NAME = 0;
    public static final int FROM_LAT_LON = 1;
    private static final String TAG = "ApiDataRepository";
    private MutableLiveData<WeatherDataModel> currentWeatherLiveData;
    private WeatherDataModel currentData;

    private List<WeatherDataModel> currentForeCasts;


    private MutableLiveData<List<WeatherDataModel>> currentForeCastsLiveData;

    public ApiDataRepository() {
        currentData = new WeatherDataModel();
        currentForeCasts = new ArrayList<>();
        currentWeatherLiveData = new MutableLiveData<>();
        currentForeCastsLiveData = new MutableLiveData<>();
        fetchRestApiCurrentWeatherData("90.42", "24.17", ApiDataRepository.FROM_LAT_LON); //TODO: Get Current Location
        fetchRestApiForecastData("90.42", "24.17", ApiDataRepository.FROM_LAT_LON);

    }

    public void fetchRestApiCurrentWeatherData(String param1, String param2, int MODE) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        final Map<String, String> mapdata = new HashMap<>();


        if (MODE == FROM_LAT_LON) {
            mapdata.put("lat", param2);
            mapdata.put("lon", param1);
        } else if (MODE == FROM_CITY_NAME) {
            mapdata.put("q", param1 + "," + param2);
        } else {
            return;
        }
        mapdata.put("APPID", "31de1dc4507ce4031564ab9b4a1532f2");

        Call<JsonObject> call = apiInterface.getCurrentWeatherApiData(mapdata);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    /*
                    Log.w("WEATHER", response.body().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").toString());
                    Log.w("WEATHER", response.body().get("wind").getAsJsonObject().get("speed").toString());
                    Log.w("WEATHER", response.body().get("main").getAsJsonObject().get("temp").toString());
                    Log.w("WEATHER", response.body().get("clouds").getAsJsonObject().get("all").toString());
                    */

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

    public void fetchRestApiForecastData(String param1, String param2, int MODE) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        final Map<String, String> mapdata = new HashMap<>();


        if (MODE == FROM_LAT_LON) {
            mapdata.put("lat", param2);
            mapdata.put("lon", param1);
        } else if (MODE == FROM_CITY_NAME) {
            mapdata.put("q", param1 + "," + param2);
        } else {
            return;
        }
        mapdata.put("APPID", "31de1dc4507ce4031564ab9b4a1532f2");

        Call<JsonObject> call = apiInterface.getWeatherForecastData(mapdata);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    int total = response.body().getAsJsonArray("list").size();
                    Log.wtf("WEATHER", String.valueOf(response.body().getAsJsonArray("list").size()));
                    parseWeatherForecast(response);


                    //parseCurrentWeatherJson(response);
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

    private void parseWeatherForecast(Response<JsonObject> response) {
        int total = response.body().getAsJsonArray("list").size();

        currentForeCasts.clear();

        for (int i = 0; i < total/2; i++) {
            JsonElement item = response.body().getAsJsonArray("list").get(i);
            String desc = item.getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").toString();
            String windSpeed = item.getAsJsonObject().get("wind").getAsJsonObject().get("speed").toString();
            String temperature = item.getAsJsonObject().get("main").getAsJsonObject().get("temp").toString();
            String clouds = item.getAsJsonObject().get("clouds").getAsJsonObject().get("all").toString();

            WeatherDataModel newWeatherData = new WeatherDataModel();
            newWeatherData.setError(false);
            newWeatherData.setClouds(clouds);
            newWeatherData.setDescription(desc);
            newWeatherData.setTemperature(temperature);
            newWeatherData.setWindSpeed(windSpeed);
            currentWeatherLiveData.setValue(currentData);
            currentForeCasts.add(newWeatherData);

        }
        Log.wtf(TAG,"Total Forecasts Found:"+currentForeCasts.size());
        currentForeCastsLiveData.setValue(currentForeCasts);
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
        currentWeatherLiveData.setValue(currentData);

    }


    public MutableLiveData<WeatherDataModel> getCurrentWeatherLiveData() {
        return currentWeatherLiveData;
    }

    public MutableLiveData<List<WeatherDataModel>> getCurrentForeCastsLiveData() {
        return currentForeCastsLiveData;
    }

}
