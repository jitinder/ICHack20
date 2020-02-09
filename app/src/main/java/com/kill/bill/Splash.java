package com.kill.bill;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    private AnimationDrawable loadingAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView loading = findViewById(R.id.imageView);
        loading.setBackgroundResource(R.drawable.loading);
        loadingAnimation = (AnimationDrawable) loading.getBackground();
        loadingAnimation.start();
    }



}
