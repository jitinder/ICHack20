package com.kill.bill;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PayLoadParser extends AppCompatActivity {
  private List<Item> items;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pay_load_parser);

    this.items = new ArrayList<>();

    ItemAdapter adapter = new ItemAdapter(this, this.items);

    ListView listView = findViewById(R.id.transactions);
    listView.setAdapter(adapter);

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
      File directory = new File(getFilesDir() + "/output");

      if (directory.listFiles() == null) {
        return;
      }

      for (File f : Objects.requireNonNull(directory.listFiles())) {
        InputStream stream = new FileInputStream(f.getAbsolutePath());
        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        root = parser.parse(reader).getAsJsonObject();JsonArray recogResults = root.getAsJsonArray("recognitionResults");

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

    } catch (ClassCastException | IOException e) {
      e.printStackTrace();
    }


  }
}
