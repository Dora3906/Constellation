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

import bean.Fortune;

public class Fragment_Week extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week,container,false);

        initViews(view);
        return view;
    }

    private static void initViews(View view) {

        ImageView ivConstellation;
        TextView constellation;
        TextView tvDate;
        TextView tvHealth;
        TextView tvMoney;
        TextView tvCareer;
        TextView tvLove;

        ivConstellation = view.findViewById(R.id.ivConstellation);
        constellation = view.findViewById(R.id.constellation);
        tvDate = view.findViewById(R.id.tvDate);
        tvHealth = view.findViewById(R.id.tvHealth);
        tvMoney = view.findViewById(R.id.tvMoney);
        tvCareer = view.findViewById(R.id.tvCareer);
        tvLove = view.findViewById(R.id.tvLove);

        Fortune fortune = DataActivity.fortune;
        ivConstellation.setImageResource(DataActivity.constellations[DataActivity.astroid-1]);
        constellation.setText(DataActivity.constellationsName[DataActivity.astroid-1]);
        tvDate.setText(fortune.getWeek().get("date").toString());
        tvHealth.setText(fortune.getWeek().get("health").toString());
        tvMoney.setText(fortune.getWeek().get("money").toString());
        tvCareer.setText(fortune.getWeek().get("career").toString());
        tvLove.setText(fortune.getWeek().get("love").toString());
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
