package com.nimrag.kevin.aweweico.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nimrag.kevin.aweweico.R;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (FragmentTabHost)findViewById(R.id.tab_container);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.main_content);
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("tab1"), MainFragment.class, null);
        //hide tabs
        mTabHost.getTabWidget().getChildAt(0).setVisibility(View.GONE);
    }
}
