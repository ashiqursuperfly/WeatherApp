package com.ashiqur.weatherapp.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashiqur.weatherapp.ApiDataRepository;
import com.ashiqur.weatherapp.R;
import com.ashiqur.weatherapp.rest_api.models.WeatherDataModel;
import com.ashiqur.weatherapp.utils.ViewModelUtils;
import com.bumptech.glide.Glide;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private static final String TAG = "MainActivity";

    private LocationManager locationManager;
    private MainActivityViewModel mainActivityViewModel;
    private TextView tvTemperature, tvDescription, tvCloud, tvWindSpeed, tvCityName,tvSunrise,tvSunset,tvPressure,tvHumidity;
    private ForecastsDataAdapter adapter;
    private EditText etCityName, etCountryId;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setupViewModel();
        initPermissions();
        findDeviceLocation();
        Toast.makeText(getApplicationContext(), "Location:" + ApiDataRepository.latitude + "," + ApiDataRepository.longitude, Toast.LENGTH_LONG).show();
        Log.wtf(TAG, ApiDataRepository.latitude + "," + ApiDataRepository.longitude);

    }

    private void setupViewModel() {
        mainActivityViewModel = (MainActivityViewModel) ViewModelUtils.GetViewModel(MainActivity.this, MainActivityViewModel.class);
        mainActivityViewModel.getCurrentWeatherData().observe(MainActivity.this, new Observer<WeatherDataModel>() {
            @Override
            public void onChanged(WeatherDataModel weatherDataModel) {
                tvTemperature.setText(weatherDataModel.getTemperature());
                tvDescription.setText(weatherDataModel.getDescription());
                tvCloud.setText("Cloud: " + weatherDataModel.getClouds() + "%");
                tvWindSpeed.setText("Wind Speed: " + weatherDataModel.getWindSpeed());
                tvCityName.setText(weatherDataModel.getLocationName());
                tvSunrise.setText("Sunrise: "+weatherDataModel.getSunrise());
                tvSunset.setText("Sunset: "+weatherDataModel.getSunset());
                tvPressure.setText("Pressure: "+weatherDataModel.getPressure());
                tvHumidity.setText("Humidity: "+weatherDataModel.getHumidity());

                Glide.with(MainActivity.this).asBitmap().load(weatherDataModel.getImageUrl()).into(imageView);

            }
        });
        mainActivityViewModel.getForecastsData().observe(this, new Observer<List<WeatherDataModel>>() {
            @Override
            public void onChanged(List<WeatherDataModel> weatherDataModels) {
                adapter.setData(weatherDataModels);
            }
        });
    }

    private void initPermissions() {
        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

    }

    private void findDeviceLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Check gps is enable or not
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Write Function To enable gps
            gpsEnable();
        } else {
            //GPS is already On then
            getLocation();
        }
    }

    private void initViews() {

        imageView = findViewById(R.id.image);

        tvTemperature = findViewById(R.id.tv_temperature);
        tvCloud = findViewById(R.id.tv_cloud);
        tvDescription = findViewById(R.id.tv_desc);
        tvWindSpeed = findViewById(R.id.tv_wind_speed);
        tvSunrise = findViewById(R.id.tv_sunrise);
        tvSunset = findViewById(R.id.tv_sunset);
        tvPressure = findViewById(R.id.tv_pressure);
        tvHumidity = findViewById(R.id.tv_humidity);

        Button btnAnyWeather = findViewById(R.id.btn_find_weather);
        tvCityName = findViewById(R.id.tv_city_name);


        etCityName = findViewById(R.id.et_city_name);
        etCountryId = findViewById(R.id.et_country_id);

        btnAnyWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = etCityName.getText().toString().trim(), countryId = etCountryId.getText().toString().trim();

                if (cityName.equals("") || countryId.equals(""))
                    Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_LONG).show();
                else {
                    mainActivityViewModel.fetchCurrentWeatherDataFromCityName(cityName, countryId.toUpperCase());
                    mainActivityViewModel.fetchCurrentForecastsDataFromCityName(cityName, countryId.toUpperCase());
                }
            }
        });

        Button btnDeviceLocationWeather = findViewById(R.id.btn_find_device_location_weather);
        btnDeviceLocationWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDeviceLocation();
                if (ApiDataRepository.longitude != null || ApiDataRepository.latitude != null) {
                    mainActivityViewModel.fetchCurrentWeatherData(ApiDataRepository.longitude, ApiDataRepository.latitude);
                    mainActivityViewModel.fetchCurrentForecastsData(ApiDataRepository.longitude, ApiDataRepository.latitude);

                } else
                    Toast.makeText(getApplicationContext(), "Error Finding Current Location Weather.", Toast.LENGTH_LONG).show();
            }
        });
        initRecyclerView();
    }

    void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);

        adapter = new ForecastsDataAdapter(this);

        recyclerView.setAdapter(adapter);




    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps != null) {
                double lat = LocationGps.getLatitude();
                double longi = LocationGps.getLongitude();

                ApiDataRepository.latitude = String.valueOf(lat);
                ApiDataRepository.longitude = String.valueOf(longi);

            } else if (LocationNetwork != null) {
                double lat = LocationNetwork.getLatitude();
                double longi = LocationNetwork.getLongitude();

                ApiDataRepository.latitude = String.valueOf(lat);
                ApiDataRepository.longitude = String.valueOf(longi);

            } else if (LocationPassive != null) {
                double lat = LocationPassive.getLatitude();
                double longi = LocationPassive.getLongitude();

                ApiDataRepository.latitude = String.valueOf(lat);
                ApiDataRepository.longitude = String.valueOf(longi);

            } else {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void gpsEnable() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
