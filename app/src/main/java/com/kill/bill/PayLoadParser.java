package com.kill.bill;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PayLoadParser extends AppCompatActivity {

  private final List<Item> items;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pay_load_parser);
    
    this.items = new ArrayList<>();
    
    ItemAdapter itemAdapter = new ItemAdapter(this, items);
    listView.setAdapter(itemAdapter);

    try {
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
      root = parser.parse(new FileReader(filename)).getAsJsonObject();
    } catch (FileNotFoundException | ClassCastException e) {
      e.printStackTrace();
      return;
    }

    JsonArray recogResults = root.getAsJsonArray("recognitionResults");

    for (int page = 0; page < recogResults.size(); page++) {
      JsonObject pageLines = recogResults.get(page).getAsJsonObject();
      JsonArray lines = pageLines.getAsJsonArray("lines");

      for (int line = 0; line < lines.size(); line++) {
        Item item = new Item();
        JsonElement lineData = lines.get(line).getAsJsonObject().get("text");

        System.out.println(lineData);

        item.setName(lineData.toString());
        this.items.add(item);
      }
   }
}
