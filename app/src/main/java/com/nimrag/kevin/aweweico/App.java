package com.nimrag.kevin.aweweico;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.nimrag.kevin.aweweico.lib.GlobalContext;
import com.nimrag.kevin.aweweico.sinasdk.bean.UserInfo;

import okhttp3.OkHttpClient;

/**
 * Created by kevin on 2017/2/28.
 */

public class App extends GlobalContext {
    private static UserInfo mUserInfo;

    /**
     * since_id max_id分别用于更新、加载更多
     */
    private static long sinceId;
    private static long maxId;

    public static long getSinceId() {return sinceId;}
    public static void setSinceId(long id) {sinceId = id;}
    public static long getMaxId() {return maxId;}
    public static void setMaxId(long id) {maxId = id;}

    public static UserInfo getUserInfo() {
        if (mUserInfo == null) {
            mUserInfo = new UserInfo();
            SharedPreferences userInfoPreference = getGlobalContext().getSharedPreferences("UserInfo", Activity.MODE_PRIVATE);
            if (userInfoPreference != null) {
                mUserInfo.setUid(userInfoPreference.getString("uid", ""));
                mUserInfo.setAccessToken(userInfoPreference.getString("access_token", ""));
                mUserInfo.setRemindIn(userInfoPreference.getString("remind_in", ""));
                mUserInfo.setExpiresIn(userInfoPreference.getInt("expires_in", 0));
            }
        }
        return mUserInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        SharedPreferences userInfoPreference = getGlobalContext().getSharedPreferences("UserInfo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfoPreference.edit();
        editor.putString("uid", userInfo.getUid());
        editor.putString("access_token", userInfo.getAccessToken());
        editor.putString("remind_in", userInfo.getRemindIn());
        editor.putInt("expires_in", userInfo.getExpiresIn());
        editor.commit();
    }
}
