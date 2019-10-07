package com.ashiqur.weatherapp;

import com.ashiqur.weatherapp.rest_api.models.CityDataModel;

import java.util.List;

public class CountryDataRepository {
    private List<CityDataModel> allCities;

    public CountryDataRepository() {
        //TODO:parse cities.JSON file
        allCities.add(new CityDataModel("1","Los Angeles County","US"));
        allCities.add(new CityDataModel("2","Peshawar","PK"));
        allCities.add(new CityDataModel("3","California","US"));
    }



}
