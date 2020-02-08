package com.kill.bill;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class TestAzureActivity extends AppCompatActivity {

    private static final String subscriptionKey = "87e56323493740c7a179825fa8cfc9ed";// System.getenv("KILL_BILL_SUB_KEY");
    private static final String endpoint = ("https://killbillcomputervision.cognitiveservices.azure.com/");


    private static final String readURI = endpoint +
            "vision/v2.1/read/core/asyncBatchAnalyze";

    private static final String imageToAnalyze =
            "https://i.imgur.com/TQXB8ds.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_azure);

        Log.e("SUBKEY", "" + subscriptionKey);


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    URL readURI = new URL(TestAzureActivity.readURI);
                    HttpsURLConnection readConnection =
                            (HttpsURLConnection) readURI.openConnection();

                    String myData = "{\"url\":\"" + imageToAnalyze + "\"}";

                    readConnection.setRequestMethod("POST");
                    readConnection.setRequestProperty("Content-Type", "application/json");
                    readConnection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);

                    // Enable writing
                    readConnection.setDoOutput(true);

                    // Write the data
                    readConnection.getOutputStream().write(myData.getBytes());

                    String outputEndpoint = "";

                    int response = readConnection.getResponseCode();
                    if ( response == 200) {
                        Log.d("CHECK", "200");
                    } else if ( response == 202) {
                        Log.d("CHECK", "202");

                        outputEndpoint = readConnection.getHeaderField("Operation-Location");

                    } else{
                        // Error handling code goes here
                        Log.e("CHECK", "Failed with " + response);
                        Log.e("CHECK", "Failed with " + readConnection);
                    }

                    Log.d("CHECK", outputEndpoint);


                    Thread.sleep(1000);
                    //We should probably mke this an async promise of some kind that pings the server repeatedly with a nice twirling loading animation


                    URL jsonURI = new URL(outputEndpoint);
                    HttpsURLConnection jsonConnection =
                            (HttpsURLConnection) jsonURI.openConnection();

                    jsonConnection.setRequestMethod("GET");

                    jsonConnection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);


                    response = jsonConnection.getResponseCode();
                    if ( response == 200) {
                        Log.d("CHECKOUT", "200");
                        outputEndpoint = jsonConnection.getHeaderField("Content-Length");

                        InputStream responseBody = jsonConnection.getInputStream();
                        InputStreamReader responseBodyReader =
                                new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReader = new JsonReader(responseBodyReader);

                        jsonReader.beginObject(); // Start processing the JSON object
                        while (jsonReader.hasNext()) { // Loop through all keys
                            String key = jsonReader.nextName(); // Fetch the next key
                            if (key.equals("status")) {
                                String value = jsonReader.nextString();

                                Log.i("JSON", value);

                                if (!value.equals("Succeeded")){
                                    throw new RuntimeException("Failed to parse text from image: status was not Succeeded");
                                }
                                break;
                            } else {
                                jsonReader.skipValue(); // Skip values of other keys
                            }
                        }

                        jsonReader.close();
                        jsonConnection.disconnect();

                    } else if ( response == 202) {
                        Log.d("CHECKOUT", "202");

                    } else{
                        // Error handling code goes here
                        Log.e("CHECKOUT", "Failed with " + response);
                        Log.e("CHECKOUT", "Failed with " + jsonConnection);
                    }

                    Log.d("CHECKOUTLENGTH", outputEndpoint);



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });



    }
}
