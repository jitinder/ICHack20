package com.kill.bill;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

public class TestAzureActivity extends AppCompatActivity {

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
          takePhoto();
          galleryAddPic();
        });
  }

  public void takePhoto() {
    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    //File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");

    //imageUri = Uri.fromFile(photo);
    //intent.putExtra(MediaStore.EXTRA_OUTPUT, this.imageUri);

    this.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    /* If scanning image */
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
      /* Gets bitmap from intent */
      Bundle extras = data != null ? data.getExtras() : null;
      Bitmap image = (Bitmap) (extras != null ? extras.get("data") : null);

      /* Display bitmap onscreen */
      //((ImageView) findViewById(R.id.image)).setImageBitmap(image);

      if (image != null) {
        /* Buffer for storing bytes from bitmap*/
        int size = image.getRowBytes() * image.getHeight();
        ByteBuffer buffer = ByteBuffer.allocate(size);
        byte[] array;

        /* Builds Firebase Blob from byte[] */
        image.copyPixelsToBuffer(buffer);
        array = buffer.array();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference imageRef = storageReference.child("scanned_images/pic.jpg");

        try {
          AsyncTask.execute(() -> {
            try {
              Tasks.await(imageRef.putBytes(array));
            } catch (ExecutionException | InterruptedException e) {
              e.printStackTrace();
            }
          });

          Uri downloadUri = imageRef.getDownloadUrl().getResult();
          System.out.println(downloadUri != null ? downloadUri.toString() : null);
        } catch (Exception e) {
          System.out.println("This is fun");
          e.printStackTrace();
        }
      }
    }
  }

  private void galleryAddPic() {
    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    File f = new File(String.valueOf(imageUri));
    Uri contentUri = Uri.fromFile(f);

    mediaScanIntent.setData(contentUri);

    this.sendBroadcast(mediaScanIntent);
  }
}
