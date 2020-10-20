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

public class Fragment_Tomorrow extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tomorrow,container,false);

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

        ivConstellation = (ImageView) view.findViewById(R.id.ivConstellation);
        constellation = (TextView) view.findViewById(R.id.constellation);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        rbSummary = (RatingBar) view.findViewById(R.id.rbSummary);
        rbLove = (RatingBar) view.findViewById(R.id.rbLove);
        rbCareer = (RatingBar) view.findViewById(R.id.rbCareer);
        rbMoney = (RatingBar) view.findViewById(R.id.rbMoney);
        rbHealth = (RatingBar) view.findViewById(R.id.rbHealth);
        tvNumber = (TextView) view.findViewById(R.id.tvNumber);
        tvColor = (TextView) view.findViewById(R.id.tvColor);
        tvStar = (TextView) view.findViewById(R.id.tvStar);
        tvPreSummary = (TextView) view.findViewById(R.id.tvPreSummary);

        Fortune fortune = DataActivity.fortune;
        ivConstellation.setImageResource(DataActivity.constellations[DataActivity.astroid-1]);
        constellation.setText(DataActivity.constellationsName[DataActivity.astroid-1]);
        tvDate.setText(fortune.getTomorrow().get("date").toString());
        rbSummary.setRating(Float.parseFloat(fortune.getTomorrow().get("summary").toString()));
        rbLove.setRating(Float.parseFloat(fortune.getTomorrow().get("love").toString()));
        rbCareer.setRating(Float.parseFloat(fortune.getTomorrow().get("career").toString()));
        rbMoney.setRating(Float.parseFloat(fortune.getTomorrow().get("money").toString()));
        rbHealth.setRating(Float.parseFloat(fortune.getTomorrow().get("health").toString()));
        tvNumber.setText(fortune.getTomorrow().get("number").toString());
        tvColor.setText(fortune.getTomorrow().get("color").toString());
        tvStar.setText(fortune.getTomorrow().get("star").toString());
        tvPreSummary.setText(fortune.getTomorrow().get("presummary").toString());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
