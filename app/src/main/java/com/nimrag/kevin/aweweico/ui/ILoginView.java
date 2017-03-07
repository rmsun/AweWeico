package com.nimrag.kevin.aweweico.ui;

import android.webkit.WebView;

import com.nimrag.kevin.aweweico.sinasdk.bean.UserInfo;

/**
 * Created by kevin on 2017/2/27.
 */

public interface ILoginView {
    void onLoginResult(boolean result, UserInfo info);
    WebView getWebView();
}
