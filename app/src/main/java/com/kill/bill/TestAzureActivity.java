package com.kill.bill;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class TestAzureActivity extends AppCompatActivity {

    private static final String subscriptionKey = "87e56323493740c7a179825fa8cfc9ed"; //System.getenv("87e56323493740c7a179825fa8cfc9ed");
    private static final String endpoint = ("https://killbillcomputervision.cognitiveservices.azure.com/");


    private static final String uriBase = endpoint +
            "vision/v2.1/read/core/asyncBatchAnalyze";

    private static final String imageToAnalyze =
            "https://i.imgur.com/TQXB8ds.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_azure);

        /*
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(uriBase)
        .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        */

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // All your networking logic
                // should be here


                try {
                    URL githubEndpoint = new URL(uriBase);
                    HttpsURLConnection myConnection =
                            (HttpsURLConnection) githubEndpoint.openConnection();

                    String myData = "{\"url\":\"https://i.imgur.com/TQXB8ds.jpg\"}";

                    myConnection.setRequestMethod("POST");

                    myConnection.setRequestProperty("Content-Type", "application/json");

                    myConnection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);

                    // Enable writing
                    myConnection.setDoOutput(true);

                    // Write the data
                    myConnection.getOutputStream().write(myData.getBytes());


                    int response = myConnection.getResponseCode();
                    if ( response == 200) {
                        Log.d("CHECK", "200");
                    } else if ( response == 202) {
                        Log.d("CHECK", "202");
                        InputStream responseBody = myConnection.getInputStream();

                        InputStreamReader responseBodyReader =
                                new InputStreamReader(responseBody, "UTF-8");

                        Log.d("CHECK", "" + responseBodyReader.read());
                        myConnection.get
                        Log.d("CHECK", "" + myConnection.getResponseMessage());



                        StringBuilder textBuilder = new StringBuilder();

                        try (Reader reader = new BufferedReader(new InputStreamReader(responseBody, Charset.forName(StandardCharsets.UTF_8.name())))){
                            Log.d("CHECK", "try");

                            int c = 0;
                            while ((c = reader.read()) != -1) {
                                Log.d("CHECK", "while");
                                textBuilder.append((char) c);
                            }
                        } catch (Exception e){

                        }

                        Log.d("CHECK", textBuilder.toString());
                        //InputStreamReader responseBodyReader =
                        //       new InputStreamReader(responseBody, "UTF-8");
                    } else{
                        // Error handling code goes here
                        Log.e("CHECK", "Failed with " + response);
                        Log.e("CHECK", "Failed with " + myConnection);
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });



    }
}
