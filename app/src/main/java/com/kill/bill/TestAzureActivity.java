package com.kill.bill;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class TestAzureActivity extends AppCompatActivity {

    private static final String subscriptionKey = "87e56323493740c7a179825fa8cfc9ed";
    private static final String endpoint = "https://killbillcomputervision.cognitiveservices.azure.com/";

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static Uri imageToAnalyze;

    private static final String readURI = endpoint + "vision/v2.1/read/core/asyncBatchAnalyze";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_azure);

        Log.e("SUBKEY", "" + subscriptionKey);

        new GetImageText(this).execute(readURI, null, "");

        Button takeImageButton = findViewById(R.id.take_picture_button);
        takeImageButton.setOnClickListener(
                view -> {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    this.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                });

        Button goToPayload = findViewById(R.id.go_to_payload);
        goToPayload.setOnClickListener(
                view -> {
                    Intent intent = new Intent(view.getContext(), PayLoadParser.class);
                    startActivity(intent);
                });
    }

    public void onClick2(View v) {
        startActivity(new Intent(this, TransactionList.class));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Bitmap bm;

            if (data == null || data.getExtras() == null) {
                return;
            }

            bm = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");

            ((ImageView) findViewById(R.id.image)).setImageBitmap(bm);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.ENGLISH);

            imageToAnalyze = Uri.parse(sdf.format(Calendar.getInstance().getTime()));

            File file;

            try {
                file =
                        File.createTempFile(
                                "/scanned_picture:" + imageToAnalyze.toString(), ".jpg", getFilesDir());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            StorageReference imageRef =
                    storageReference.child("scanned/" + imageToAnalyze.toString() + ".jpg");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            if (bm == null) {
                return;
            }

            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();

            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }

            UploadTask task = imageRef.putFile(Uri.fromFile(file));

            task.addOnFailureListener(o -> System.out.println("Failure"))
                    .addOnSuccessListener(o -> {
                        System.out.println("Success");
                        try {
                            Uri url = Tasks.await(imageRef.getDownloadUrl());

                            Log.e("imageRefURI", "" + url.toString());
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    private static class GetImageText extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog;

        public GetImageText(Activity activity) {
            this.dialog = new ProgressDialog(activity);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL readURI = new URL(strings[0]);
                HttpsURLConnection readConnection = (HttpsURLConnection) readURI.openConnection();

                imageToAnalyze = Uri.parse("https://i.imgur.com/TQXB8ds.jpg");

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



                URL jsonURI = new URL(outputEndpoint);
                HttpsURLConnection jsonConnection = (HttpsURLConnection) jsonURI.openConnection();

                jsonConnection.setRequestMethod("GET");

                jsonConnection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);

                response = jsonConnection.getResponseCode();
                if (response == 200) {
                    boolean parsed = false;
                    while (!parsed){
                        Log.d("CHECKOUT", "200");

                        jsonConnection = (HttpsURLConnection) jsonURI.openConnection();

                        jsonConnection.setRequestMethod("GET");

                        jsonConnection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);

                        InputStream responseBody = jsonConnection.getInputStream();
                        InputStreamReader responseBodyReader =
                                new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                        BufferedReader br = new BufferedReader(responseBodyReader);
                        StringBuilder sb = new StringBuilder();

                        String inputLine;
                        while ((inputLine = br.readLine()) != null) {
                            sb.append(inputLine);
                            System.out.println(inputLine);
                        }
                        Log.d("CHECKOUT", sb.toString());
                        if (sb.substring(0,Math.min(20, sb.length())).contains("Succeeded")){
                            parsed=true;
                        }
                        else{
                            jsonConnection.disconnect();
                            jsonConnection.connect();
                            Thread.sleep(50);
                        }

                    }
                    Log.d("CHECKOUT", "200");

                    InputStream responseBody = jsonConnection.getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(responseBodyReader);
                    StringBuilder sb = new StringBuilder();

                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                        System.out.println(inputLine);
                    }

                    br.close();

                    // System.out.println(sb.toString());
                    jsonConnection.disconnect();

                    return sb.toString();

                } else if (response == 202) {
                    Log.d("CHECKOUT", "202");

                } else {
                    // Error handling code goes here
                    Log.e("CHECKOUT", "Failed with " + response);
                    Log.e("CHECKOUT", "Failed with " + jsonConnection);
                }

                Log.d("CHECKOUTLENGTH: ", outputEndpoint);

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
            System.out.println("Payload: " + s);
        }
    }
}
