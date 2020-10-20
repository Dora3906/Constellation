package com.example.constellation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 14323
 */
public class Fragment_Home extends Fragment {
    private ViewPager viewPager;
    private List<Fragment> pages = new ArrayList<>();
    private View view = null;
    private TabLayout tabLayout;
    private String[] titles = new String[]{"今日运势","明日运势","本周运势","本月运势","本年运势"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null){
            view = inflater.inflate(R.layout.fragment_home,container,false);

            initViews(view);
        }
        return view;
    }

    @SuppressLint("InflateParams")
    private void initViews(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        pages.add(new Fragment_Today());
        pages.add(new Fragment_Tomorrow());
        pages.add(new Fragment_Week());
        pages.add(new Fragment_Month());
        pages.add(new Fragment_Year());

        viewPager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return pages.get(position);
        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
