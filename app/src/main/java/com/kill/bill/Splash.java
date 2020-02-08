package com.kill.bill;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    private AnimationDrawable loadingAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        ImageView loading = findViewById(R.id.imageViewReceipt);
        loading.setImageResource(R.drawable.loading);
        //loading.setBackgroundResource(R.drawable.loading);
        loadingAnimation = (AnimationDrawable) loading.getDrawable();
        loadingAnimation.start();
    }



}
