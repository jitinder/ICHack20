package com.kill.bill;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
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
import android.os.Bundle;
import android.net.Uri;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TestAzureActivity extends AppCompatActivity {

    private static final String subscriptionKey = "87e56323493740c7a179825fa8cfc9ed";// System.getenv("KILL_BILL_SUB_KEY");
    private static final String endpoint = ("https://killbillcomputervision.cognitiveservices.azure.com/");


    private static final String readURI = endpoint +
            "vision/v2.1/read/core/asyncBatchAnalyze";

    private static final String imageToAnalyze =
            "https://i.imgur.com/TQXB8ds.jpg";


    private String downloadEndpoint =
            "";

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



                    int response = readConnection.getResponseCode();
                    if ( response == 200) {
                        Log.d("CHECK", "200");
                    } else if ( response == 202) {
                        Log.d("CHECK", "202");

                        downloadEndpoint = readConnection.getHeaderField("Operation-Location");

                    } else{
                        // Error handling code goes here
                        Log.e("CHECK", "Failed with " + response);
                        Log.e("CHECK", "Failed with " + readConnection);
                    }

                    Log.d("CHECK", downloadEndpoint);


                    Thread.sleep(1000);
                    //We should probably mke this an async promise of some kind that pings the server repeatedly with a nice twirling loading animation


                    //final ProgressDialog progressDialog = new ProgressDialog(TestAzureActivity.this);

                    URL jsonURI = new URL(downloadEndpoint);
                    HttpsURLConnection jsonConnection =
                            (HttpsURLConnection) jsonURI.openConnection();

                    jsonConnection.setRequestMethod("GET");

                    jsonConnection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);


                    response = jsonConnection.getResponseCode();
                    if ( response == 200) {
                        Log.d("CHECKOUT", "200");
                        downloadEndpoint = jsonConnection.getHeaderField("Content-Length");

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

                                if (!value.equals("Succeeded")) {
                                    throw new RuntimeException("Failed to parse text from image: status was not Succeeded");
                                }
                            } else if (key.equals("recognitionResults")) {
                                jsonReader.beginArray();
                                jsonReader.beginObject();

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

                        Log.d("CHECKOUTLENGTH", downloadEndpoint);
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (ProtocolException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

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



//  private class DownloadTask extends AsyncTask<Void, Integer, String> {
//    @Override
//    protected String doInBackground(Void...voids) {
//      String result = "";
//
//      BufferedReader reader = null;
//
//      publishProgress(0);
//
//      try {
//          URL jsonURI = new URL(downloadEndpoint);
//          HttpsURLConnection jsonConnection =
//                  (HttpsURLConnection) jsonURI.openConnection();
//
//          jsonConnection.setRequestMethod("GET");
//
//          jsonConnection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);
//
//
//          int response = jsonConnection.getResponseCode();
//          if ( response == 200) {
//              Log.d("CHECKOUT", "200");
//              downloadEndpoint = jsonConnection.getHeaderField("Content-Length");
//
//              InputStream responseBody = jsonConnection.getInputStream();
//              InputStreamReader responseBodyReader =
//                      new InputStreamReader(responseBody, "UTF-8");
//
//              JsonReader jsonReader = new JsonReader(responseBodyReader);
//
//              jsonReader.beginObject(); // Start processing the JSON object
//              while (jsonReader.hasNext()) { // Loop through all keys
//                  String key = jsonReader.nextName(); // Fetch the next key
//                  if (key.equals("status")) {
//                      String value = jsonReader.nextString();
//
//                      Log.i("JSON", value);
//
//                      if (!value.equals("Succeeded")) {
//                          throw new RuntimeException("Failed to parse text from image: status was not Succeeded");
//                      }
//                  } else if (key.equals("recognitionResults")) {
//                      jsonReader.beginArray();
//                      jsonReader.beginObject();
//
//                  } else {
//                      jsonReader.skipValue(); // Skip values of other keys
//                  }
//              }
//
//
//              jsonReader.close();
//              jsonConnection.disconnect();
//
//
//          } else if ( response == 202) {
//              Log.d("CHECKOUT", "202");
//
//          } else{
//              // Error handling code goes here
//              Log.e("CHECKOUT", "Failed with " + response);
//              Log.e("CHECKOUT", "Failed with " + jsonConnection);
//          }
//
//          Log.d("CHECKOUTLENGTH", downloadEndpoint);
//
//
//
//        //connection.getContentType() should return something like "application/json; charset=utf-8"
//        String[] values = connection.getContentType().toString().split(";");
//        String charset = "";
//        for (String value : values) {
//          value = value.trim();
//          if (value.toLowerCase().startsWith("charset=")) {
//            charset = value.substring("charset=".length());
//            break;
//          }
//        }
//        //Set default value if charset not set
//        if(charset.equals("")) charset = "utf-8";
//
//        int contentLength = connection.getContentLength();
//        InputStream stream = connection.getInputStream();
//        reader  = new BufferedReader(new InputStreamReader(stream));
//        StringBuilder builder = new StringBuilder();
//        /**
//         * connection.getContentLength() can return -1 on some connections.
//         * If we have the content length calculate progress, else just set progress to 100 and build the string all at once.
//         *
//         */
//        if(contentLength>-1){
//          //Odd byte array sizes don't always work, tried 512, 1024, 2048; 1024 is the magic number because it seems to work best.
//          byte[] data = new byte[1024];
//          int totalRead = 0;
//          int bytesRead = 0;
//          while ((bytesRead = stream.read(data)) > 0) {
//            try {
//              builder.append(new String(data, 0, bytesRead, charset));
//            } catch (UnsupportedEncodingException e) {
//              Log.e(TAG, "Invalid charset: " + e.getMessage());
//              //Append without charset (uses system's default charset)
//              builder.append(new String(data, 0, bytesRead));
//            }
//            totalRead += bytesRead;
//            int progress = (int) (totalRead * (100/(double) contentLength));
//            //Log.d(TAG, "length = " + contentLength + " bytesRead = " + bytesRead + " totalRead = " + totalRead + " progress = " + progress);
//            publishProgress(progress);
//          }
//        } else {
//          String line = "";
//          while ((line = reader.readLine()) != null) {
//            builder.append(line + '\n');
//            publishProgress(100);
//          }
//        }
//        result = builder.toString();
//
//      } catch (Exception e) {
//        Log.e(TAG, "Exception "+e.toString());
//      } finally {
//        if(reader != null){
//          try {
//            reader.close();
//          } catch(IOException ioe) {
//            ioe.printStackTrace();
//          }
//        }
//      }
//      return result;
//    }
//
//    protected void onProgressUpdate(Integer... progress) {
//      if(!this.isCancelled()) command.onProgressUpdate(progress[0]);
//    }
//
//    @Override
//    protected void onPostExecute(String result){
//      if(!this.isCancelled()) downloadTaskComplete(result);
//    }
//  }
}
