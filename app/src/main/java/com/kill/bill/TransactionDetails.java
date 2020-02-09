package com.kill.bill;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class TransactionDetails extends AppCompatActivity {
  private Item transaction;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
      getSupportActionBar().hide();
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
              WindowManager.LayoutParams.FLAG_FULLSCREEN);
    Bundle args = getIntent().getExtras();
    transaction = (Item) Objects.requireNonNull(Objects.requireNonNull(args).get("item"));

    Objects.requireNonNull(getSupportActionBar()).hide();

    getWindow()
        .setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    setContentView(R.layout.activity_transaction_details);
  }


    public void onClick(View v){
        startActivity(new Intent(this, TransactionList.class));
    }


}
