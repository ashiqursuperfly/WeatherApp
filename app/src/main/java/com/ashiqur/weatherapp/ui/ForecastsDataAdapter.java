package com.ashiqur.weatherapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashiqur.weatherapp.R;
import com.ashiqur.weatherapp.rest_api.models.WeatherDataModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ForecastsDataAdapter extends RecyclerView.Adapter<ForecastsDataAdapter.DataViewHolder> {
    private final Context context;
    private List<WeatherDataModel> data = new ArrayList<>();

    public ForecastsDataAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_horizontal_item, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        WeatherDataModel currentNote = data.get(position);
        holder.textViewDesc.setText(currentNote.getDescription());
        holder.textViewWindSpeed.setText(currentNote.getWindSpeed());
        holder.textViewCloud.setText(String.valueOf(currentNote.getClouds()));
        holder.textViewTemperature.setText(currentNote.getTemperature());
        holder.textViewDate.setText(currentNote.getDate());
        Glide.with(context).asBitmap().load(currentNote.getImageUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public WeatherDataModel getNoteAt(int index) {
        return data.get(index);
    }

    public void setData(List<WeatherDataModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDesc,textViewWindSpeed,textViewCloud,textViewTemperature,textViewDate;
        private ImageView image;

        public DataViewHolder(View itemView) {
            super(itemView);
            textViewDesc = itemView.findViewById(R.id.tv_desc);
            textViewWindSpeed = itemView.findViewById(R.id.tv_wind_speed);
            textViewCloud = itemView.findViewById(R.id.tv_cloud);
            textViewTemperature = itemView.findViewById(R.id.tv_temperature);
            textViewDate = itemView.findViewById(R.id.tv_date);
            image = itemView.findViewById(R.id.imageView);
        }
    }
}
