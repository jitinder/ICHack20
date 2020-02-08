package com.kill.bill;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class TestAzureActivity extends AppCompatActivity {

    private static final String subscriptionKey = System.getenv("COMPUTER_VISION_SUBSCRIPTION_KEY");
    private static final String endpoint = ("COMPUTER_VISION_ENDPOINT");


    private static final String uriBase = endpoint +
            "vision/v2.1/read/core/asyncBatchAnalyze";

    private static final String imageToAnalyze =
            "https://i.imgur.com/TQXB8ds.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_azure);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();



    }
}
