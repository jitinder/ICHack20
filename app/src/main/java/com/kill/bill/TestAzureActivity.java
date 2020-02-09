package com.kill.bill;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class TestAzureActivity extends AppCompatActivity {

  private static final String baseUrl = "gs://killbill-8ef96.appspot.com/scanned_images";

  private static final String subscriptionKey = System.getenv("COMPUTER_VISION_SUBSCRIPTION_KEY");
  private static final String endpoint = ("COMPUTER_VISION_ENDPOINT");

  private static final int REQUEST_IMAGE_CAPTURE = 1;

  private Uri imageUri;

  private static FirebaseAuth auth;

  private static final String uriBase = endpoint + "vision/v2.1/read/core/asyncBatchAnalyze";

  private static final String imageToAnalyze = "https://i.imgur.com/TQXB8ds.jpg";

  GoogleSignInClient client;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test_azure);

    auth = FirebaseAuth.getInstance();

    /*Retrofit retrofit =
    new Retrofit.Builder()
        .baseUrl("https://api.imgur.com/3/image")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();*/

    Button takeImageButton = findViewById(R.id.takePictureButton);

    takeImageButton.setOnClickListener(
        view -> {
          StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
          StrictMode.setVmPolicy(builder.build());
          imageUri = takePhoto();
        });
  }

  public Uri takePhoto() {
    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    Uri uri = Uri.fromFile(new File(getFilesDir() + "/pic.jpg"));

    intent.putExtra(MediaStore.EXTRA_OUTPUT, this.imageUri);

    this.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

    return uri;
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

      imageUri =
          Uri.parse(
                  sdf.format(Calendar.getInstance().getTime()));

      File file;

      try {
        file = File.createTempFile(
                "/scanned_picture:"
                + imageUri.toString(), ".jpg", getFilesDir());
      } catch (IOException e) {
        e.printStackTrace();
        return;
      }

      FirebaseStorage storage = FirebaseStorage.getInstance();
      StorageReference storageReference = storage.getReference();
      StorageReference imageRef = storageReference.child("scanned/" + imageUri.toString() + ".jpg");

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
          .addOnSuccessListener(o -> System.out.println("Success"));
    }
  }
}
