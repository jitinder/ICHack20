package com.kill.bill;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
  private static final String endpoint =
      "https://killbillcomputervision.cognitiveservices.azure.com/";

  private static final int REQUEST_IMAGE_CAPTURE = 1;

  private static Uri imageToAnalyze;

  private static final String readURI = endpoint + "vision/v2.1/read/core/asyncBatchAnalyze";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test_azure);

    Toolbar toolbar = findViewById(R.id.azure_toolbar);
    setSupportActionBar(toolbar);

    Log.e("SUBKEY", "" + subscriptionKey);

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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_IMAGE_CAPTURE) {
      Bitmap bm;

      if (data == null || data.getExtras() == null) {
        return;
      }

      Intent intent = new Intent(TestAzureActivity.this, Splash.class);
      intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
      startActivity(intent);

      bm = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");

      // ((ImageView) findViewById(R.id.image)).setImageBitmap(bm);

      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.ENGLISH);

      imageToAnalyze = Uri.parse(sdf.format(Calendar.getInstance().getTime()));

      File file;
      String prefix = imageToAnalyze.toString();

      try {
        File outputDir = TestAzureActivity.this.getCacheDir(); // context being the Activity pointer

        file = File.createTempFile(prefix, "jpg", outputDir);
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
          .addOnSuccessListener(
              o -> {
                System.out.println("Success");

                AsyncTask.execute(
                    () -> {
                      try {
                        Uri url = Tasks.await(imageRef.getDownloadUrl());

                        Log.e("imageRefURI", "" + url.toString());

                        GetImageText executor = new GetImageText();
                        executor.execute(new Holder(new String[]{readURI, url.toString(), null, ""}, getFilesDir()));

                      } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                      }
                    });
              });
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();

    inflater.inflate(R.menu.action_bar_menu, menu);

    return true;
  }

  private static class Holder {
    private String[] strings;
    private File directory;

    public Holder(String[] strings, File directory) {
      this.strings = strings;
      this.directory = directory;
    }
  }

  private static class GetImageText extends AsyncTask<Holder, Void, String> {

    @Override
    protected String doInBackground(Holder... holders) {
      try {
        URL readURI = new URL(holders[0].strings[0]);
        HttpsURLConnection readConnection = (HttpsURLConnection) readURI.openConnection();

        imageToAnalyze = Uri.parse(holders[0].strings[1]);

        String myData = "{\"url\":\"" + imageToAnalyze + "\"}";

        readConnection.setRequestMethod("POST");
        readConnection.setRequestProperty("Content-Type", "application/json");
        readConnection.setRequestProperty("X_HTTP_Method-Override", "PATCH");
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
        HttpsURLConnection jsonConnection = (HttpsURLConnection) jsonURI.openConnection();

        jsonConnection.setRequestMethod("GET");

        jsonConnection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);

        response = jsonConnection.getResponseCode();
        if (response == 200) {
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

          File directory = new File((holders[0].directory) + "/output");

          if (!directory.exists()) {
            Log.i("CREATING NEW FILE: ", String.valueOf(directory.mkdir()));
          }

          FileOutputStream fos = new FileOutputStream(holders[0].directory + "/output/" + imageToAnalyze.toString() + ".json"); // Tokenise to get only between scanned and .jpg
          fos.write(sb.toString().getBytes());

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
      // dialog.setMessage("Analysing your receipt");
      // dialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
      super.onPostExecute(s);
      // dialog.dismiss();
      System.out.println("Payload: " + s);
      /*Intent intent = new Intent(TestAzureActivity.this, PayLoadParser.class);
      startActivity(intent);*/
    }
  }
}
