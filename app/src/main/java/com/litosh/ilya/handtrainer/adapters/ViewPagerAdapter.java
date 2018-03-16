package com.litosh.ilya.handtrainer.adapters;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created by ilya_ on 16.03.2018.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private List<View> pages;

    public ViewPagerAdapter(List<View> pages){
        this.pages = pages;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(View collection, int position){
        View v = pages.get(position);
        ((ViewPager) collection).addView(v, 0);
        return v;
    }
}
