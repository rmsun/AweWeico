package com.nimrag.kevin.aweweico.ui;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nimrag.kevin.aweweico.R;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabHost;
    private ImageView tabHome;
    private ImageView tabMsg;
    private ImageView tabDiscovery;
    private ImageView tabPofile;
    private FrameLayout slideLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (FragmentTabHost)findViewById(R.id.tab_container);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.main_content);
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("tab1"), MainFragment.class, null);
        //hide tabs
        mTabHost.getTabWidget().getChildAt(0).setVisibility(View.GONE);

        slideLayout = (FrameLayout)findViewById(R.id.container_fragment_list);
        View view = getLayoutInflater().inflate(R.layout.slide_fragment_layout, slideLayout, true);
        //slideLayout.addView(view);

        tabHome = (ImageView)findViewById(R.id.tab_icons_home_img);
        tabHome.setSelected(true);
        tabMsg = (ImageView)findViewById(R.id.tab_icons_msg_img);
        tabDiscovery = (ImageView)findViewById(R.id.tab_icons_disc_img);
        tabPofile = (ImageView)findViewById(R.id.tab_icons_prof_img);
    }
}
