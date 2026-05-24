package com.example.wearagain.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wearagain.R;
import com.example.wearagain.databinding.ActivitySplashBinding;
import com.example.wearagain.ui.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int trajanjeSplasha = 3000;
    private ActivitySplashBinding vezanje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vezanje = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(vezanje.getRoot());

        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeInSlow = AnimationUtils.loadAnimation(this, R.anim.fade_in_slow);

        vezanje.logoSlika.startAnimation(scaleUp);
        vezanje.nazivAplikacije.startAnimation(slideUp);
        vezanje.podNaslov.startAnimation(fadeIn);
        vezanje.tvVerzija.startAnimation(fadeInSlow);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, trajanjeSplasha);
    }
}