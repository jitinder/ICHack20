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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestAzureActivity extends AppCompatActivity {

  private static final String baseUrl = "gs://killbill-8ef96.appspot.com/scanned_images";

  private static final String subscriptionKey = System.getenv("COMPUTER_VISION_SUBSCRIPTION_KEY");
  private static final String endpoint = ("COMPUTER_VISION_ENDPOINT");

  private static final int REQUEST_IMAGE_CAPTURE = 1;

  private Uri imageUri;

  private static final String uriBase = endpoint + "vision/v2.1/read/core/asyncBatchAnalyze";

  private static final String imageToAnalyze = "https://i.imgur.com/TQXB8ds.jpg";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test_azure);

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

  /*private void galleryAddPic() {
    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    File f = new File(String.valueOf(imageUri));
    Uri contentUri = Uri.fromFile(f);

    mediaScanIntent.setData(contentUri);

    this.sendBroadcast(mediaScanIntent);
  }*/

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    Bitmap bm = (Bitmap) data.getExtras().get("data");
    ((ImageView) findViewById(R.id.image)).setImageBitmap(bm);

    File file = new File(getFilesDir() + "/pic.jpg");

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    StorageReference imageRef = storageReference.child("scanned/pic.jpg");

    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
    byte[] bytes = stream.toByteArray();

    try {
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(bytes);
    } catch (IOException e) {
      e.printStackTrace();
    }


    UploadTask task = imageRef.putFile(Uri.fromFile(file));
    task.pause();
    task.resume();

    task.addOnFailureListener(o -> System.out.println("Failure"))
        .addOnSuccessListener(o -> System.out.println("Success"));
  }
}
