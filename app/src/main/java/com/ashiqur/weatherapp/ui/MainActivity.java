package com.ashiqur.weatherapp.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ashiqur.weatherapp.R;
import com.ashiqur.weatherapp.rest_api.models.WeatherDataModel;
import com.ashiqur.weatherapp.utils.ViewModelUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    private TextView tvTemperature,tvDescription,tvCloud,tvWindSpeed;
    private Button btnRefresh;
    private ForecastsDataAdapter adapter;

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
        mainActivityViewModel.getForecastsData().observe(this, new Observer<List<WeatherDataModel>>() {
            @Override
            public void onChanged(List<WeatherDataModel> weatherDataModels) {
                adapter.setNotes(weatherDataModels);
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
        initRecyclerView();
    }

    void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ForecastsDataAdapter();
        recyclerView.setAdapter(adapter);

//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
//                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
//                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
//            }
//        }).attachToRecyclerView(recyclerView);

    }


}
