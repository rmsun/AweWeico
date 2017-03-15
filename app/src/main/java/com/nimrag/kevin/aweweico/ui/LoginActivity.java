package com.nimrag.kevin.aweweico.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.nimrag.kevin.aweweico.App;
import com.nimrag.kevin.aweweico.R;
import com.nimrag.kevin.aweweico.sinasdk.bean.UserInfo;

/**
 * Created by kevin on 2017/2/22.
 */

public class LoginActivity extends AppCompatActivity implements ILoginView{

    WebView mWebView;
    ILoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_login);

        mWebView = (WebView)findViewById(R.id.webView);
        mLoginPresenter = new LoginPresenter(this);
        TextView mLoginBtn = (TextView) findViewById(R.id.sso_button);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.setVisibility(View.VISIBLE);
                mLoginPresenter.doLogin();
            }
        });
    }

    public void onLoginResult(boolean success, UserInfo info) {
        if (success) {
            App.setUserInfo(info);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public WebView getWebView() {
        return mWebView;
    }
}
