package com.kill.bill;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.Blob;

import java.nio.ByteBuffer;


public class ScanActivity extends AppCompatActivity {
  /* Magic number for camera intent */
  private static final int REQUEST_IMAGE_CAPTURE = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scan);
  }

  public void onClick(View view) {
    /* Switches to Android camera */
    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    if (takePicture.resolveActivity(getPackageManager()) != null) {
      startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    /* If scanning image */
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
      /* Gets bitmap from intent */
      Bundle extras = data != null ? data.getExtras() : null;
      Bitmap image = (Bitmap) (extras != null ? extras.get("data") : null);

      /* Display bitmap onscreen */
      ((ImageView) findViewById(R.id.image)).setImageBitmap(image);

      if (image != null) {
        /* Buffer for storing bytes from bitmap*/
        int size = image.getRowBytes() * image.getHeight();
        ByteBuffer buffer = ByteBuffer.allocate(size);
        byte[] array;
        Blob blob;

        /* Builds Firebase Blob from byte[] */
        image.copyPixelsToBuffer(buffer);
        array = buffer.array();
        blob = Blob.fromBytes(array);

        System.out.println("Success converting image");

        // TODO: function call with blob
      }
    }
  }
}
