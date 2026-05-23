package com.example.wearagain.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import com.example.wearagain.R;
import com.example.wearagain.ui.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int trajanjeSplasha = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, trajanjeSplasha);
    }
}
