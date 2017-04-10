package com.nimrag.kevin.aweweico.ui;

import android.app.WallpaperManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nimrag.kevin.aweweico.R;
import com.nimrag.kevin.aweweico.lib.ActivitySharePrefHelper;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabHost;
    private ImageView tabHome;
    private ImageView tabMsg;
    private ImageView tabDiscovery;
    private ImageView tabPofile;
    private FrameLayout slideLayout;
    String [] groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (FragmentTabHost)findViewById(R.id.tab_container);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.main_content);
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("tab1"), MainFragment.class, null);
        // hide tabs
        mTabHost.getTabWidget().getChildAt(0).setVisibility(View.GONE);

        slideLayout = (FrameLayout)findViewById(R.id.container_fragment_list);
        View view = getLayoutInflater().inflate(R.layout.slide_fragment_layout, slideLayout, true);
        ImageView profileImage = (ImageView) view.findViewById(R.id.current_avatar);
        String profileImageUrl = ActivitySharePrefHelper.getShareData(getApplicationContext(), "user_profile_image_url");
        if(!profileImageUrl.isEmpty()) {
            Picasso.with(getApplicationContext()).load(profileImageUrl).into(profileImage);
        }
        // 获取桌面背景作为slide fragment的背景
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        view.setBackground(wallpaperDrawable);

        groups = getResources().getStringArray(R.array.fixed_group_names);
        ListView groupList = (ListView)findViewById(R.id.index_left_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.slide_group_list_item, groups);
        groupList.setAdapter(adapter);

        tabHome = (ImageView)findViewById(R.id.tab_icons_home_img);
        tabHome.setSelected(true);
        tabMsg = (ImageView)findViewById(R.id.tab_icons_msg_img);
        tabDiscovery = (ImageView)findViewById(R.id.tab_icons_disc_img);
        tabPofile = (ImageView)findViewById(R.id.tab_icons_prof_img);
    }
}
