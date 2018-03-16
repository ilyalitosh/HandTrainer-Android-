package com.litosh.ilya.handtrainer;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.litosh.ilya.handtrainer.adapters.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LayoutInflater layoutInflater;
    private List<View> pages;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutInflater = getLayoutInflater();
        pages = new ArrayList<>();
        View trainerPageView = layoutInflater.inflate(R.layout.trainer_page, null);
        pages.add(trainerPageView);
        View statsPageView = layoutInflater.inflate(R.layout.stats_page, null);
        pages.add(statsPageView);

        adapter = new ViewPagerAdapter(pages);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

    }
}
