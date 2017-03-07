package com.nimrag.kevin.aweweico;

import android.app.Application;

import okhttp3.OkHttpClient;

/**
 * Created by kevin on 2017/2/28.
 */

public class App extends Application {

    private static App mContext;
    private static OkHttpClient mClient;

    @Override
    public void onCreate() {
        mContext = this;
    }

    public static App getGlobalContext() {
        return mContext;
    }

    public static OkHttpClient getHttpClient() {
        if (mClient == null) {
            mClient = new OkHttpClient();
        }
        return mClient;
    }
}
