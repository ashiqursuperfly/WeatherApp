package com.ashiqur.weatherapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashiqur.weatherapp.R;
import com.ashiqur.weatherapp.rest_api.models.WeatherDataModel;

import java.util.ArrayList;
import java.util.List;

public class ForecastsDataAdapter extends RecyclerView.Adapter<ForecastsDataAdapter.DataViewHolder> {
    private List<WeatherDataModel> notes = new ArrayList<>();

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        WeatherDataModel currentNote = notes.get(position);
        holder.textViewDesc.setText(currentNote.getDescription());
        holder.textViewWindSpeed.setText(currentNote.getWindSpeed());
        holder.textViewCloud.setText(String.valueOf(currentNote.getClouds()));
        holder.textViewTemperature.setText(currentNote.getTemperature());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public WeatherDataModel getNoteAt(int index) {
        return notes.get(index);
    }

    public void setNotes(List<WeatherDataModel> data) {
        this.notes = data;
        notifyDataSetChanged();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDesc;
        private TextView textViewCloud;
        private TextView textViewWindSpeed;
        private TextView textViewTemperature;

        public DataViewHolder(View itemView) {
            super(itemView);
            textViewDesc = itemView.findViewById(R.id.tv_desc);
            textViewWindSpeed = itemView.findViewById(R.id.tv_wind_speed);
            textViewCloud = itemView.findViewById(R.id.tv_cloud);
            textViewTemperature = itemView.findViewById(R.id.tv_temperature);
        }
    }
}
