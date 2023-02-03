package com.example.rexpos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rexpos.adapters.KitchenViewPagerAdapter;
import com.example.rexpos.customViewPagers.NonSwipeableViewPager;

public class KitchenActivity extends BaseActivity implements View.OnClickListener {


    KitchenViewPagerAdapter kitchenViewPagerAdapter;
    NonSwipeableViewPager mKitchenViewPager;

    ImageView btnPrew, btnNext;
    TextView mTextPre, mTextNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_kitchen);

        btnPrew = findViewById(R.id.btn_preview);
        btnPrew.setOnClickListener(this);
        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        mTextPre = findViewById(R.id.txt_pre);
        mTextNext = findViewById(R.id.txt_next);

        kitchenViewPagerAdapter = new KitchenViewPagerAdapter(this);
        mKitchenViewPager = findViewById(R.id.vp_kitchen_screen);
        mKitchenViewPager.setAdapter(kitchenViewPagerAdapter);
    }


    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        int currentIndex = mKitchenViewPager.getCurrentItem();

        switch (v.getId()){
            case R.id.btn_preview:
                if (currentIndex > 0) {
                    mKitchenViewPager.setCurrentItem(currentIndex - 1, true);
                }
                break;
            case R.id.btn_next:
                if (currentIndex < mKitchenViewPager.getAdapter().getCount() - 1) {
                    mKitchenViewPager.setCurrentItem(currentIndex + 1, true);
                }
                break;
        }


    }
}
