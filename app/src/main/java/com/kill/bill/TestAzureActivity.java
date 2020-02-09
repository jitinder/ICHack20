package com.kill.bill;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.JsonReader;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.content.Intent;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.concurrent.ExecutionException;

import android.os.Bundle;
import android.net.Uri;

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

        new GetImageText(this).execute(readURI, null, "");

        Button takeImageButton = findViewById(R.id.takePictureButton);
        takeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                takePhoto();
                galleryAddPic();
            }
        });

        Button goToPayload = findViewById(R.id.gottopayload);
        goToPayload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PayLoadParser.class);
                startActivity(intent);
            }
        });
    }

    Uri imageUri;

    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        this.startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(String.valueOf(imageUri));
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private class GetImageText extends AsyncTask<String, Void, String>{
        private ProgressDialog dialog;

        public GetImageText(Activity activity) {
            this.dialog = new ProgressDialog(activity);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL readURI = new URL(strings[0]);
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
                if (response == 200) {
                    Log.d("CHECK", "200");
                } else if (response == 202) {
                    Log.d("CHECK", "202");

                    outputEndpoint = readConnection.getHeaderField("Operation-Location");

                } else {
                    // Error handling code goes here
                    Log.e("CHECK", "Failed with " + response);
                    Log.e("CHECK", "Failed with " + readConnection);
                }

                Log.d("CHECK", outputEndpoint);

                Thread.sleep(2000);
                
                URL jsonURI = new URL(outputEndpoint);
                HttpsURLConnection jsonConnection =
                        (HttpsURLConnection) jsonURI.openConnection();

                jsonConnection.setRequestMethod("GET");

                jsonConnection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);


                response = jsonConnection.getResponseCode();
                if (response == 200) {
                    Log.d("CHECKOUT", "200");

                    InputStream responseBody = jsonConnection.getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");
                    BufferedReader br = new BufferedReader(responseBodyReader);
                    StringBuilder sb = new StringBuilder();

                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                        System.out.println(inputLine);
                    }

                    br.close();

                    //System.out.println(sb.toString());
                    jsonConnection.disconnect();

                    return sb.toString();

                } else if (response == 202) {
                    Log.d("CHECKOUT", "202");

                } else {
                    // Error handling code goes here
                    Log.e("CHECKOUT", "Failed with " + response);
                    Log.e("CHECKOUT", "Failed with " + jsonConnection);
                }

                Log.d("CHECKOUTLENGTH", outputEndpoint);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Analysing your receipt");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            System.out.println("Payload: " +s);
        }
    }
}
