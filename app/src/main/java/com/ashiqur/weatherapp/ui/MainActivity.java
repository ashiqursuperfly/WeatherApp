package com.ashiqur.weatherapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ashiqur.weatherapp.R;
import com.ashiqur.weatherapp.utils.ViewModelUtils;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        mainActivityViewModel = (MainActivityViewModel) ViewModelUtils.GetViewModel(MainActivity.this, MainActivityViewModel.class);


    }

    private void initViews() {

    }


}
