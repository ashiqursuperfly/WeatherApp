package com.ashiqur.weatherapp.rest_api.models;

public class WeatherDataModel {
    private String temperature,windSpeed,description,clouds;
    private boolean isError = false;

    public WeatherDataModel() {
    }

    public WeatherDataModel(String temperature, String windSpeed, String description, String clouds) {
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.description = description;
        this.clouds = clouds;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWindSpeed() {
        return windSpeed;
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
}

