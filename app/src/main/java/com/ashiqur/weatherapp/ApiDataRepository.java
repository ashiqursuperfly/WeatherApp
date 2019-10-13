package com.ashiqur.weatherapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ashiqur.weatherapp.rest_api.ApiClient;
import com.ashiqur.weatherapp.rest_api.models.WeatherDataModel;
import com.ashiqur.weatherapp.rest_api.service.ApiInterface;
import com.ashiqur.weatherapp.utils.GPSUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
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

        String latitude = GPSUtils.getInstance().getLatitude(), longitude = GPSUtils.getInstance().getLongitude();

        if (latitude != null && longitude != null) {
            fetchRestApiCurrentWeatherData(longitude, latitude, ApiDataRepository.FROM_LAT_LON);
            fetchRestApiForecastData(longitude, latitude, ApiDataRepository.FROM_LAT_LON);
        } else {
            fetchRestApiCurrentWeatherData("123.42", "24.17", ApiDataRepository.FROM_LAT_LON);
            fetchRestApiForecastData("123.42", "24.17", ApiDataRepository.FROM_LAT_LON);
        }
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
                //TODO: showToast Error Msg
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
                    parseWeatherForecast(response);
                } else {
                    Log.wtf(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //TODO: Show Toast Error Msg
                //currentData.setError(true);
                Log.wtf(TAG, "OnFailure:" + t.getMessage());
            }
        });
    }

    private void parseWeatherForecast(Response<JsonObject> response){
        if (response.body() == null) return;

        int total = response.body().getAsJsonArray("list").size();

        currentForeCasts.clear();

        for (int i = 0; (i < total); i++) {

            if (i % 4 != 0) continue;

            JsonElement item = response.body().getAsJsonArray("list").get(i);
            String desc = item.getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").toString();
            String iconId = item.getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("icon").toString().trim();
            String windSpeed = item.getAsJsonObject().get("wind").getAsJsonObject().get("speed").toString();
            String temperature = item.getAsJsonObject().get("main").getAsJsonObject().get("temp").toString();
            String clouds = item.getAsJsonObject().get("clouds").getAsJsonObject().get("all").toString();
            String dateText = item.getAsJsonObject().get("dt_txt").toString();
            String imageUrl = "http://openweathermap.org/img/wn/" + iconId.substring(1, iconId.length() - 1) + "@2x.png";
            // http://openweathermap.org/img/wn/10d@2x.png
            Log.wtf(TAG, imageUrl);
            WeatherDataModel newWeatherData = new WeatherDataModel();
            newWeatherData.setError(false);
            newWeatherData.setClouds(clouds);
            newWeatherData.setDescription(desc);
            newWeatherData.setTemperature(temperature);
            newWeatherData.setWindSpeed(windSpeed);
            newWeatherData.setDate(dateText);
            newWeatherData.setImageUrl(imageUrl);
            currentWeatherLiveData.setValue(currentData);
            currentForeCasts.add(newWeatherData);

        }
        Log.wtf(TAG, "Total Forecasts Found:" + currentForeCasts.size());
        currentForeCastsLiveData.setValue(currentForeCasts);
    }


    private void parseCurrentWeatherJson(Response<JsonObject> response) {
        if (response.body() == null) return;

        // weather stats
        String desc = response.body().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").toString();
        String windSpeed = response.body().get("wind").getAsJsonObject().get("speed").toString();
        String temperature = response.body().get("main").getAsJsonObject().get("temp").toString();
        String pressure = response.body().get("main").getAsJsonObject().get("pressure").toString();
        String humidity = response.body().get("main").getAsJsonObject().get("humidity").toString();
        String clouds = response.body().get("clouds").getAsJsonObject().get("all").toString();
        String sunrise = response.body().get("sys").getAsJsonObject().get("sunrise").toString();
        String sunset = response.body().get("sys").getAsJsonObject().get("sunset").toString();


        // misc
        String cityName = response.body().get("name").getAsString();
        String countryId = response.body().getAsJsonObject("sys").get("country").getAsString();
        String dateText = response.body().get("dt").getAsString();
        String iconId = response.body().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("icon").toString().trim();
        String imageUrl = "http://openweathermap.org/img/wn/" + iconId.substring(1, iconId.length() - 1) + "@2x.png";
        currentData.setError(false);
        currentData.setClouds(clouds);
        currentData.setDescription(desc);
        currentData.setTemperature(temperature);
        currentData.setWindSpeed(windSpeed);
        currentData.setPressure(pressure);
        currentData.setHumidity(humidity);
        currentData.setSunrise(new Date(Long.parseLong(sunrise) * 1000L).toString());
        currentData.setSunset(new Date(Long.parseLong(sunset) * 1000L).toString());

        currentData.setLocationName(cityName + "," + countryId);
        currentData.setImageUrl(imageUrl);
        currentData.setDate(new Date(Long.parseLong(dateText) * 1000L).toString());

        currentWeatherLiveData.setValue(currentData);


    }


    public MutableLiveData<WeatherDataModel> getCurrentWeatherLiveData() {
        return currentWeatherLiveData;
    }

    public MutableLiveData<List<WeatherDataModel>> getCurrentForeCastsLiveData() {
        return currentForeCastsLiveData;
    }

}
