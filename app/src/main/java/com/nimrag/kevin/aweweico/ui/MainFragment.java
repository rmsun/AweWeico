package com.nimrag.kevin.aweweico.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nimrag.kevin.aweweico.R;
import com.nimrag.kevin.aweweico.lib.ActivitySharePrefHelper;
import com.nimrag.kevin.aweweico.sinasdk.SinaSDK;
import com.nimrag.kevin.aweweico.sinasdk.bean.FriendsTimeLine;
import com.squareup.picasso.Picasso;

/**
 * Created by kevin on 2017/2/14.
 */

public class MainFragment extends Fragment {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    TextView userScreenName;
    private static final int NUM_PAGES = 1;

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new TimeLineHomeFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mPager = (ViewPager)v.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        // 用户昵称
        userScreenName = (TextView)v.findViewById(R.id.index_title_group);
        String nick = ActivitySharePrefHelper.getShareData(getActivity().getApplicationContext(), "user_nick");
        if (!nick.isEmpty()) {
            userScreenName.setText(nick);
        } else {
            GetUserDetailInfoTask task = new GetUserDetailInfoTask();
            task.execute();
        }
        return v;
    }

    // TODO:移到presenter中,UI只负责展示，不处理逻辑
    private class GetUserDetailInfoTask extends AsyncTask<Void, Void, FriendsTimeLine.StatusesBean.UserBean> {
        @Override
        protected FriendsTimeLine.StatusesBean.UserBean doInBackground(Void... params) {
            return SinaSDK.getInstance().getUserDetailInfo();
        }

        @Override
        protected void onPostExecute(FriendsTimeLine.StatusesBean.UserBean userDetailInfo) {
            super.onPostExecute(userDetailInfo);
            if (userDetailInfo != null) {
                userScreenName.setText(userDetailInfo.getScreen_name());
                ActivitySharePrefHelper.putShareData(getActivity().getApplicationContext(), "user_nick", userDetailInfo.getScreen_name());
                ActivitySharePrefHelper.putShareData(getActivity().getApplicationContext(), "user_profile_image_url", userDetailInfo.getAvatar_large());
            }
        }
    }
}
