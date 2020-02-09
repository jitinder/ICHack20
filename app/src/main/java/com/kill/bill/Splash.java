package com.kill.bill;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        ImageView loading = findViewById(R.id.image_view_receipt);

        loading.setImageResource(R.drawable.loading);

        AnimationDrawable loadingAnimation = (AnimationDrawable) loading.getDrawable();
        loadingAnimation.start();
    }



}
