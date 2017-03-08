package com.nimrag.kevin.aweweico.lib;

import android.app.Application;

import okhttp3.OkHttpClient;

/**
 * Created by kevin on 2017/3/8.
 */

public class GlobalContext extends Application {
    private static GlobalContext mContext;
    private static OkHttpClient mClient;

    @Override
    public void onCreate() {
        mContext = this;
    }

    public static GlobalContext getGlobalContext() {
        return mContext;
    }
    public static GlobalContext getInstance() { return mContext; }

    public static OkHttpClient getHttpClient() {
        if (mClient == null) {
            mClient = new OkHttpClient();
        }
        return mClient;
    }
}
