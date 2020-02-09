package com.kill.bill;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class TransactionDetails extends AppCompatActivity {
  private Item transaction;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle args = getIntent().getExtras();
    transaction = (Item) Objects.requireNonNull(Objects.requireNonNull(args).get("item"));

    Objects.requireNonNull(getSupportActionBar()).hide();

    getWindow()
        .setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    setContentView(R.layout.activity_transaction_details);
  }

  public void setTransaction(Item transaction) {
    this.transaction = transaction;
  }
}
