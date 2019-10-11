package com.ashiqur.weatherapp.rest_api.models;


import java.math.BigDecimal;

public class WeatherDataModel {
    private String temperature;
    private String windSpeed;
    private String description;
    private String clouds;
    private String locationName;
    private String date;
    private String imageUrl;
    private boolean isError = false;

    public WeatherDataModel() {
    }

    public String getTemperature() {
        BigDecimal bd = new BigDecimal(Float.parseFloat(temperature) - 273f);
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        return String.valueOf(bd.floatValue()) + '\u2103';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWindSpeed() {
        return windSpeed + "m/s";
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClouds() {
        return clouds;
    }

    public void setClouds(String clouds) {
        this.clouds = clouds;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}

