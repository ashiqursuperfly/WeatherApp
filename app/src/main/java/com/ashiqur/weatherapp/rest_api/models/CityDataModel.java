package com.ashiqur.weatherapp.rest_api.models;

public class CityDataModel {
    private String cityName,countryId,uniqueId;

    public CityDataModel() {
    }

    public CityDataModel(String uniqueId,String cityName, String countryId) {
        this.cityName = cityName;
        this.countryId = countryId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
