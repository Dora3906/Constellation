package com.example.constellation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import bean.Fortune;

public class Fragment_Today extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_today,container,false);

        initViews(view);
        return view;
    }

    private static void initViews(View view) {

        ImageView ivConstellation;
        TextView constellation;
        TextView tvDate;
        RatingBar rbSummary;
        RatingBar rbLove;
        RatingBar rbCareer;
        RatingBar rbMoney;
        RatingBar rbHealth;
        TextView tvNumber;
        TextView tvColor;
        TextView tvStar;
        TextView tvPreSummary;

        ivConstellation = view.findViewById(R.id.ivConstellation);
        constellation = view.findViewById(R.id.constellation);
        tvDate = view.findViewById(R.id.tvDate);
        rbSummary = view.findViewById(R.id.rbSummary);
        rbLove = view.findViewById(R.id.rbLove);
        rbCareer = view.findViewById(R.id.rbCareer);
        rbMoney = view.findViewById(R.id.rbMoney);
        rbHealth = view.findViewById(R.id.rbHealth);
        tvNumber = view.findViewById(R.id.tvNumber);
        tvColor = view.findViewById(R.id.tvColor);
        tvStar = view.findViewById(R.id.tvStar);
        tvPreSummary = view.findViewById(R.id.tvPreSummary);


        Fortune fortune = DataActivity.fortune;
        ivConstellation.setImageResource(DataActivity.constellations[DataActivity.astroid-1]);
        constellation.setText(DataActivity.constellationsName[DataActivity.astroid-1]);
        tvDate.setText(fortune.getToday().get("date").toString());
        rbSummary.setRating(Float.parseFloat(fortune.getToday().get("summary").toString()));
        rbLove.setRating(Float.parseFloat(fortune.getToday().get("love").toString()));
        rbCareer.setRating(Float.parseFloat(fortune.getToday().get("career").toString()));
        rbMoney.setRating(Float.parseFloat(fortune.getToday().get("money").toString()));
        rbHealth.setRating(Float.parseFloat(fortune.getToday().get("health").toString()));
        tvNumber.setText(fortune.getToday().get("number").toString());
        tvColor.setText(fortune.getToday().get("color").toString());
        tvStar.setText(fortune.getToday().get("star").toString());
        tvPreSummary.setText(fortune.getToday().get("presummary").toString());
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
