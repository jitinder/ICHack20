package com.kill.bill;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class PayLoadParser extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pay_load_parser);

    try {
      parseJSON();
    } catch (Exception e) {
      System.out.println("JSON Exception");
    }
  }

  private void parseJSON() {
    JsonObject root;
    JsonParser parser = new JsonParser();

    try {
      root = parser.parse(new FileReader("parser.json")).getAsJsonObject();
    } catch (FileNotFoundException | ClassCastException e) {
      e.printStackTrace();
      return;
    }

    JsonArray recogResults = root.getAsJsonArray("recognitionResults");

    for (int page = 0; page < recogResults.size(); page++) {
      JsonObject pageLines = recogResults.get(page).getAsJsonObject();
      JsonArray lines = pageLines.getAsJsonArray("lines");

      for (int line = 0; line < lines.size(); line++) {
        JsonObject lineData = lines.get(line).getAsJsonObject();

        System.out.println(lineData.get("text"));
      }
    }
  }
}
