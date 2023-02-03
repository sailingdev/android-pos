package com.example.rexpos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.rexpos.R;

public class KitchenViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mInflator;

    public KitchenViewPagerAdapter(Context context) {
        this.mContext = context;
        this.mInflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View layout = mInflator.inflate(R.layout.kitchen_viewpager_layout, container, false);
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }




}
