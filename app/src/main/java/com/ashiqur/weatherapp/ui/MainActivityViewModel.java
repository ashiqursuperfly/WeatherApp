package com.ashiqur.weatherapp.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ashiqur.weatherapp.ApiDataRepository;
import com.ashiqur.weatherapp.rest_api.models.WeatherDataModel;

import java.util.List;


public class MainActivityViewModel extends AndroidViewModel {

    private ApiDataRepository apiDataRepository;
    private MutableLiveData<String> tvStatusString;

    private MutableLiveData<WeatherDataModel> currentWeatherData;
    private MutableLiveData<List<WeatherDataModel>> forecastsData;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        tvStatusString = new MutableLiveData<>();
        apiDataRepository = new ApiDataRepository();
        currentWeatherData = apiDataRepository.getCurrentWeatherLiveData();
        forecastsData = apiDataRepository.getCurrentForeCastsLiveData();


    }

    public MutableLiveData<String> getTvStatusString() {
        return tvStatusString;
    }

    public void setTvStatusString(String tvStatusString) {
        this.tvStatusString.setValue(tvStatusString);
        //setValue should be called from Main Thread, postValue should be called from this
    }

    public void fetchCurrentWeatherData(String lon, String lat) {
        apiDataRepository.fetchRestApiCurrentWeatherData(lon, lat, ApiDataRepository.FROM_LAT_LON);
    }

    public void fetchCurrentWeatherDataFromCityName(String city, String countryId) {
        apiDataRepository.fetchRestApiCurrentWeatherData(city, countryId, ApiDataRepository.FROM_CITY_NAME);
    }

    public MutableLiveData<WeatherDataModel> getCurrentWeatherData() {
        return currentWeatherData;
    }

    public MutableLiveData<List<WeatherDataModel>> getForecastsData() {
        return forecastsData;
    }
}

