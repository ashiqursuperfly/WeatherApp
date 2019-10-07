package com.ashiqur.weatherapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ashiqur.weatherapp.R;
import com.ashiqur.weatherapp.rest_api.models.WeatherDataModel;
import com.ashiqur.weatherapp.utils.ViewModelUtils;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    private TextView tvTemperature,tvDescription,tvCloud,tvWindSpeed;
    private Button btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mainActivityViewModel = (MainActivityViewModel) ViewModelUtils.GetViewModel(MainActivity.this, MainActivityViewModel.class);

        mainActivityViewModel.getCurrentWeatherData().observe(MainActivity.this, new Observer<WeatherDataModel>() {
            @Override
            public void onChanged(WeatherDataModel weatherDataModel) {
                tvTemperature.setText(weatherDataModel.getTemperature());
                tvDescription.setText("Status:"+weatherDataModel.getDescription());
                tvCloud.setText("Cloud:"+weatherDataModel.getClouds()+"%");
                tvWindSpeed.setText("Wind Speed:"+weatherDataModel.getWindSpeed());
            }
        });




    }

    private void initViews() {
        tvTemperature = findViewById(R.id.tv_temperature);
        tvCloud = findViewById(R.id.tv_cloud);
        tvDescription = findViewById(R.id.tv_desc);
        tvWindSpeed = findViewById(R.id.tv_wind_speed);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityViewModel.fetchCurrentWeatherDataFromCityName("California","US");
            }
        });
    }


}
