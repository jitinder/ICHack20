package com.kill.bill;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {
    AnimationDrawable loadingAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView loading = (ImageView)findViewById(R.id.imageView);
        loadingAnimation = (AnimationDrawable)loading.getDrawable();
        loadingAnimation.start();
    }



}
