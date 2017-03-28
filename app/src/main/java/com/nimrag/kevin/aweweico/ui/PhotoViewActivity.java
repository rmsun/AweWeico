package com.nimrag.kevin.aweweico.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.nimrag.kevin.aweweico.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunkevin on 2017/3/27.
 * 以原图的方式展示图片
 */

public class PhotoViewActivity extends FragmentActivity {

    private ArrayList<String> imageUrls;
    private int initIndex;
    ViewPager viewPager;
    DotViewIndicator indicator;

    private class PhotoSlidePagerAdapter extends FragmentStatePagerAdapter {

        public PhotoSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PhotoViewFragment.newInstance(imageUrls.get(position));
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            imageUrls = (ArrayList<String>) intent.getSerializableExtra("imageUrls");
            initIndex = (int) intent.getIntExtra("initIndex", 0);
            for (int i = 0; i < imageUrls.size(); i++) {
                Log.d("haha", "largImagePic: " + imageUrls.get(i));
            }
            Log.d("haha", "initIndex: " + initIndex);
        }

        setContentView(R.layout.phote_view_layout);
        viewPager = (ViewPager)findViewById(R.id.photo_view_pager);
        viewPager.setAdapter(new PhotoSlidePagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(initIndex);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                indicator.setCurrentIndex(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        indicator = (DotViewIndicator)findViewById(R.id.dot_indicator);
        if (imageUrls.size() > 1) {
            indicator.setDotNumbers(imageUrls.size());
            indicator.setCurrentIndex(initIndex);
        }
    }
}
