package com.kill.bill;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PayLoadParser extends AppCompatActivity {
  private List<Item> items;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pay_load_parser);

    Intent intent = getIntent();
    String json = intent.getStringExtra("json");

    this.items = new ArrayList<>();

    ItemAdapter adapter = new ItemAdapter(this, this.items);

    ListView listView = findViewById(R.id.transactions);
    listView.setAdapter(adapter);

    try {
      parseJSONstring(json);
      parseJSON("example_1.json");
      parseJSON("example_2.json");
      parseJSON("example_3.json");
    } catch (Exception e) {
      System.out.println("JSON Exception");
    }
  }

  private void parseJSON(String filename) {
    JsonObject root;
    JsonParser parser = new JsonParser();

    try {
      InputStream stream = getAssets().open(filename);
      InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
      root = parser.parse(reader).getAsJsonObject();
    } catch (ClassCastException | IOException e) {
      e.printStackTrace();
      return;
    }

    JsonArray recogResults = root.getAsJsonArray("recognitionResults");

    for (int page = 0; page < recogResults.size(); page++) {
      JsonObject pageLines = recogResults.get(page).getAsJsonObject();
      JsonArray lines = pageLines.getAsJsonArray("lines");

      for (int line = 0; line < lines.size(); line++) {
        Item item = new Item(null);
        JsonElement lineData = lines.get(line).getAsJsonObject().get("text");

        System.out.println(lineData);

        item.setName(lineData.toString());
        this.items.add(item);
      }
    }
  }

  private void parseJSONstring(String json) {
    JsonObject root;
    JsonParser parser = new JsonParser();

    try {
      root = parser.parse(json).getAsJsonObject();
    } catch (ClassCastException e) {
      e.printStackTrace();
      return;
    }

    JsonArray recogResults = root.getAsJsonArray("recognitionResults");

    for (int page = 0; page < recogResults.size(); page++) {
      JsonObject pageLines = recogResults.get(page).getAsJsonObject();
      JsonArray lines = pageLines.getAsJsonArray("lines");

      for (int line = 0; line < lines.size(); line++) {
        Item item = new Item(null);
        JsonElement lineData = lines.get(line).getAsJsonObject().get("text");

        System.out.println(lineData);

        item.setName(lineData.toString());
        this.items.add(item);
      }
    }
  }
}
