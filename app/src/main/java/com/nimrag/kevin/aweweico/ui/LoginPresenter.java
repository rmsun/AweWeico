package com.nimrag.kevin.aweweico.ui;

import android.os.AsyncTask;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nimrag.kevin.aweweico.AppContext;
import com.nimrag.kevin.aweweico.sinasdk.bean.UserInfo;
import com.nimrag.kevin.aweweico.lib.Params;
import com.nimrag.kevin.aweweico.lib.ParamsUtil;
import com.nimrag.kevin.aweweico.sinasdk.SinaSDK;

/**
 * Created by kevin on 2017/2/27.
 */

public class LoginPresenter implements ILoginPresenter {

    ILoginView mView;
    WebView mWebView;

    public LoginPresenter(ILoginView v) {
        this.mView = v;
        mWebView = mView.getWebView();
        init();
    }

    private void init() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url != null && url.startsWith("https://api.weibo.com")) {
                    Params params = ParamsUtil.deCodeUrl(url);
                    String code = params.getParamValue("code");
                    getAccessTokenTask task = new getAccessTokenTask();
                    task.execute(code);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });
    }

    public void doLogin() {
        mWebView.loadUrl("https://open.weibo.cn/oauth2/authorize?client_id=295644704&redirect_uri=https://api.weibo.com/oauth2/default.html&response_type=code");
    }

    class getAccessTokenTask extends AsyncTask<String, Integer, UserInfo> {

        @Override
        protected UserInfo doInBackground(String... code) {
            try {
                return SinaSDK.getInstance().getAccessToken(code[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(UserInfo info) {
            //Log.d("haha", "uid = "+info.getUid()+" access_token = "+info.getAccessToken()+" expire_in = "+info.getExpiresIn());
            if(info.getUid() != null) {
                mView.onLoginResult(true, info);
                //AppContext.setUserInfo(info);
            } else {
                mView.onLoginResult(false, null);
            }
        }
    }
}
