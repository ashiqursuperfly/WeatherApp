package com.ashiqur.weatherapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashiqur.weatherapp.R;
import com.ashiqur.weatherapp.rest_api.models.WeatherDataModel;
import com.ashiqur.weatherapp.utils.GPSUtils;
import com.ashiqur.weatherapp.utils.ViewModelUtils;
import com.bumptech.glide.Glide;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String bgImageUrl = "https://source.unsplash.com/featured/900x1600?";
    private static final String TAG = "MainActivity";
    private GPSUtils gpsUtils;
    private MainActivityViewModel mainActivityViewModel;
    //views
    private TextView tvTemperature, tvDescription, tvCloud, tvWindSpeed, tvCityName, tvSunrise, tvSunset, tvPressure, tvHumidity;
    private ForecastsDataAdapter adapter;
    private EditText etCityName, etCountryId;
    private ImageView ivCurrentWeather, ivBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setupViewModel();
        gpsUtils = GPSUtils.getInstance();
        gpsUtils.initPermissions(MainActivity.this);

        gpsUtils.findDeviceLocation(MainActivity.this);
        Toast.makeText(getApplicationContext(), "Location:" + gpsUtils.getLatitude() + "," + gpsUtils.getLongitude(), Toast.LENGTH_LONG).show();
        Log.wtf(TAG, gpsUtils.getLatitude() + "," + gpsUtils.getLongitude());

    }

    private void setupViewModel() {
        mainActivityViewModel = (MainActivityViewModel) ViewModelUtils.GetViewModel(MainActivity.this, MainActivityViewModel.class);
        mainActivityViewModel.getCurrentWeatherData().observe(MainActivity.this, new Observer<WeatherDataModel>() {
            @Override
            public void onChanged(WeatherDataModel weatherDataModel) {
                tvCityName.setText(weatherDataModel.getLocationName());
                tvDescription.setText(weatherDataModel.getDescription());
                tvTemperature.setText(weatherDataModel.getTemperature());
                StringBuilder sb = new StringBuilder();
                sb.append("Cloud: ").append(weatherDataModel.getClouds()).append("%");
                tvCloud.setText(sb.toString());
                sb.delete(0, sb.length());

                sb.append("Wind Speed: ").append(weatherDataModel.getWindSpeed());
                tvWindSpeed.setText(sb.toString());
                sb.delete(0, sb.length());

                sb.append("Sunrise: ").append(weatherDataModel.getSunrise());
                tvSunrise.setText(sb.toString());
                sb.delete(0, sb.length());

                sb.append("Sunset: ").append(weatherDataModel.getSunset());
                tvSunset.setText(sb.toString());
                sb.delete(0, sb.length());

                sb.append("Pressure: ").append(weatherDataModel.getPressure());
                tvPressure.setText(sb.toString());
                sb.delete(0, sb.length());

                sb.append("Humidity: ").append(weatherDataModel.getHumidity());
                tvHumidity.setText(sb.toString());
                sb.delete(0, sb.length());

                Glide.with(MainActivity.this).asBitmap().load(weatherDataModel.getImageUrl()).into(ivCurrentWeather);
                //weatherDataModel.getLocationName().split(",");
                Glide.with(MainActivity.this).asBitmap().load(bgImageUrl + weatherDataModel.getLocationName()).into(ivBg);

            }
        });
        mainActivityViewModel.getForecastsData().observe(this, new Observer<List<WeatherDataModel>>() {
            @Override
            public void onChanged(List<WeatherDataModel> weatherDataModels) {
                adapter.setData(weatherDataModels);
            }
        });
    }

    private void initViews() {

        ivCurrentWeather = findViewById(R.id.image);
        ivBg = findViewById(R.id.image_view_central);

        tvTemperature = findViewById(R.id.tv_temperature);
        tvCloud = findViewById(R.id.tv_cloud);
        tvDescription = findViewById(R.id.tv_desc);
        tvWindSpeed = findViewById(R.id.tv_wind_speed);
        tvSunrise = findViewById(R.id.tv_sunrise);
        tvSunset = findViewById(R.id.tv_sunset);
        tvPressure = findViewById(R.id.tv_pressure);
        tvHumidity = findViewById(R.id.tv_humidity);

        ImageButton btnAnyWeather = findViewById(R.id.btn_find_weather);
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

        ImageButton btnDeviceLocationWeather = findViewById(R.id.btn_find_device_location_weather);
        btnDeviceLocationWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsUtils.findDeviceLocation(MainActivity.this);
                if (gpsUtils.getLongitude() != null || gpsUtils.getLatitude() != null) {
                    mainActivityViewModel.fetchCurrentWeatherData(gpsUtils.getLongitude(), gpsUtils.getLatitude());
                    mainActivityViewModel.fetchCurrentForecastsData(gpsUtils.getLongitude(), gpsUtils.getLatitude());

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
}
