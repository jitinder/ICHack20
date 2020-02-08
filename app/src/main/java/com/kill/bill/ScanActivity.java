package com.kill.bill;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

public class ScanActivity extends AppCompatActivity {
  private static final int REQUEST_IMAGE_CAPTURE = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scan);
  }

  public void onClick(View view) {
    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    if (takePicture.resolveActivity(getPackageManager()) != null) {
      startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
      Bundle extras = data != null ? data.getExtras() : null;
      Bitmap image = (Bitmap) extras.get("data");
    }
  }
}
