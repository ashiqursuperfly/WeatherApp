package com.ashiqur.weatherapp.utils;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class ViewModelUtils {

    public static ViewModel GetViewModel(FragmentActivity activity, Class<? extends ViewModel> viewModelClass){
        return ViewModelProviders.of(activity).get(viewModelClass);
    }
    public static ViewModel GetViewModel(Fragment fragment, Class<? extends ViewModel> viewModelClass){
        return ViewModelProviders.of(fragment).get(viewModelClass);
    }
}
