package com.nimrag.kevin.aweweico.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
        userInfoPreference = getSharedPreferences("UserInfo", Activity.MODE_PRIVATE);
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
