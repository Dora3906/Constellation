package com.example.constellation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;

import bean.Weather;

public class Fragment_WeatherMore extends Fragment {
    private RecyclerView recyclerView;
    private Weather weather;
    private Map<String, Integer> mapWeatherIcon;
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_weather_more, container, false);
            initViews();
        }
        return view;
    }

    private void initViews() {
        recyclerView = view.findViewById(R.id.recyclerView);
        weather = Fragment_Weather.weatherBean;
        mapWeatherIcon = Fragment_Weather.mapWeatherIcon;
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
    }

    class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder>{

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_weather_more, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.tvWeek.setText(weather.getDaily().get(position).get("week"));
            String date = weather.getDaily().get(position).get("date");
            String[] dateArr = date.split("-");
            StringBuilder sb = new StringBuilder();
            sb.append(dateArr[1]);
            sb.append("月");
            sb.append(dateArr[2]);
            sb.append("日");
            holder.tvDate.setText(sb.toString());
            holder.ivIcoDay.setImageResource(mapWeatherIcon.get(weather.getDaily().get(position).get("img_day")));
            holder.tvWeatherDay.setText(weather.getDaily().get(position).get("weather_day"));
            holder.tvTemperatureDay.setText(weather.getDaily().get(position).get("temphigh"));

            holder.tvTemperatureNight.setText(weather.getDaily().get(position).get("templow"));
            holder.ivIcoNight.setImageResource(mapWeatherIcon.get(weather.getDaily().get(position).get("img_night")));
            holder.tvWeatherNight.setText(weather.getDaily().get(position).get("weather_night"));
            holder.tvWindDirect.setText(weather.getDaily().get(position).get("winddirect_night"));
            holder.tvWindPower.setText(weather.getDaily().get(position).get("windpower_night"));
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    class MyViewHolder extends RecyclerView .ViewHolder{

        public TextView tvWeek;
        public TextView tvDate;
        public ImageView ivIcoDay;
        public TextView tvWeatherDay;
        public TextView tvTemperatureDay;
        public TextView tvTemperatureNight;
        public ImageView ivIcoNight;
        public TextView tvWeatherNight;
        public TextView tvWindDirect;
        public TextView tvWindPower;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvWeek = itemView.findViewById(R.id.tvWeek);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivIcoDay = itemView.findViewById(R.id.ivIco_day);
            tvWeatherDay = itemView.findViewById(R.id.tvWeather_day);
            tvTemperatureDay = itemView.findViewById(R.id.tvTemperature_day);
            tvTemperatureNight = itemView.findViewById(R.id.tvTemperature_night);
            ivIcoNight = itemView.findViewById(R.id.ivIco_night);
            tvWeatherNight = itemView.findViewById(R.id.tvWeather_night);
            tvWindDirect = itemView.findViewById(R.id.tvWindDirect);
            tvWindPower = itemView.findViewById(R.id.tvWindPower);

        }
    }
}
