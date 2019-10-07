package com.ashiqur.weatherapp.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ashiqur.weatherapp.ApiDataRepository;
import com.ashiqur.weatherapp.rest_api.models.WeatherDataModel;


public class MainActivityViewModel extends AndroidViewModel {

    private ApiDataRepository apiDataRepository;
    private MutableLiveData<String> tvStatusString;
    private MutableLiveData<WeatherDataModel> currentData;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        tvStatusString = new MutableLiveData<>();
        apiDataRepository = new ApiDataRepository();
        currentData = apiDataRepository.getCurrentLiveData();
    }

    public MutableLiveData<String> getTvStatusString() {
        return tvStatusString;
    }

    public void setTvStatusString(String tvStatusString) {
        this.tvStatusString.setValue(tvStatusString);
        //setValue should be called from Main Thread, postValue should be called from this
    }

    public MutableLiveData<WeatherDataModel> getCurrentData() {
        return currentData;
    }

    public void fetchRestApiData(String lon,String lat){
        apiDataRepository.fetchRestApiData(lon,lat);
    }
    public void fetchRestApiDataFromCityName(String city,String countryId){
        apiDataRepository.fetchRestApiDataFromCityName(city,countryId);
    }

}

