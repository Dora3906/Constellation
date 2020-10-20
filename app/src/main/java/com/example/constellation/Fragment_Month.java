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

public class Fragment_Month extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month,container,false);

        initViews(view);
        return view;
    }

    private static void initViews(View view) {

        ImageView ivConstellation;
        TextView constellation;
        TextView tvDate;
        TextView tvSummary;
        TextView tvMoney;
        TextView tvCareer;
        TextView tvLove;

        ivConstellation = (ImageView) view.findViewById(R.id.ivConstellation);
        constellation = (TextView) view.findViewById(R.id.constellation);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvSummary = (TextView) view.findViewById(R.id.tvSummary);
        tvMoney = (TextView) view.findViewById(R.id.tvMoney);
        tvCareer = (TextView) view.findViewById(R.id.tvCareer);
        tvLove = (TextView) view.findViewById(R.id.tvLove);

        Fortune fortune = DataActivity.fortune;
        ivConstellation.setImageResource(DataActivity.constellations[DataActivity.astroid-1]);
        constellation.setText(DataActivity.constellationsName[DataActivity.astroid-1]);
        tvDate.setText(fortune.getMonth().get("date").toString());
        tvSummary.setText(fortune.getMonth().get("summary").toString());
        tvMoney.setText(fortune.getMonth().get("money").toString());
        tvCareer.setText(fortune.getMonth().get("career").toString());
        tvLove.setText(fortune.getMonth().get("love").toString());
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
