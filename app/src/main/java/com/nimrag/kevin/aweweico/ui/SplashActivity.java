package com.nimrag.kevin.aweweico.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nimrag.kevin.aweweico.AppContext;
import com.nimrag.kevin.aweweico.R;

/**
 * Created by kevin on 2017/2/22.
 */

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences userInfoPreference;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_splash);
        AppContext.setContext(getApplicationContext());
        userInfoPreference = getSharedPreferences("UserInfo", Activity.MODE_PRIVATE);
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
               startActivity(intent);
               finish();
            }
        }, 2000);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (userInfoPreference != null && !(userInfoPreference.getString("uid", "").equals(""))) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
