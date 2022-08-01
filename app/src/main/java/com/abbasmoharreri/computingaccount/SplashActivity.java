package com.abbasmoharreri.computingaccount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("darkMode", true)) {
            setTheme(R.style.Dark_AppTheme);
        } else {
            setTheme(R.style.Light_AppTheme);
        }

        setContentView(R.layout.activity_splash);
        hideActionBar();
    }


    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }


    @Override
    protected void onStart() {
        super.onStart();
        showLogo();
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), AccountLanguageActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);


    }


    private void showLogo() {
        LottieAnimationView animationView = findViewById(R.id.lottie_animation_logo);
        animationView.setImageAssetsFolder("raw/");
        animationView.setAnimation(R.raw.logoanimation);
        animationView.loop(false);
        animationView.playAnimation();
    }


    @Override
    public void onBackPressed() {
    }
}