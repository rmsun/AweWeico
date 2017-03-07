package com.nimrag.kevin.aweweico;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.nimrag.kevin.aweweico.sinasdk.bean.UserInfo;

/**
 * Created by kevin on 2017/3/1.
 */

public class AppContext {
    private static UserInfo mUserInfo;
    private static Context mContext;

    public static void setContext(Context context) {mContext = context;}

    public static UserInfo getUserInfo() {
        if (mUserInfo == null) {
            mUserInfo = new UserInfo();
            SharedPreferences userInfoPreference = mContext.getSharedPreferences("UserInfo", Activity.MODE_PRIVATE);
            if (userInfoPreference != null) {
                mUserInfo.setUid(userInfoPreference.getString("uid", ""));
                mUserInfo.setAccessToken(userInfoPreference.getString("access_token", ""));
                mUserInfo.setRemindIn(userInfoPreference.getString("remind_in", ""));
                mUserInfo.setExpiresIn(userInfoPreference.getInt("expires_in", 0));
            }
        }
        return mUserInfo;
    }

    public static void setUserInfo(UserInfo userInfo, Context context) {
        mContext = context;
        mUserInfo = userInfo;
        SharedPreferences userInfoPreference = context.getSharedPreferences("UserInfo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfoPreference.edit();
        editor.putString("uid", userInfo.getUid());
        editor.putString("access_token", userInfo.getAccessToken());
        editor.putString("remind_in", userInfo.getRemindIn());
        editor.putInt("expires_in", userInfo.getExpiresIn());
        editor.commit();
    }
}
