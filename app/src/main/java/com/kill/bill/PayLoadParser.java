package com.kill.bill;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.regex.Pattern;

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
            //parseJSON("example_1.json");
            //parseJSON("example_2.json");
            //parseJSON("example_3.json");
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

            String cost = ".*\\d+\\. *\\d+.*";
            String justcost = "\\d+ *\\. *\\d+";
            String words = ".*\\w+[\\w\\s,.-/]*.*";
            String number = ".*\\d+.*";
            String currency = ".*[£$€]+.*";

            String specificcost = "\\d+\\.\\d\\d";


            Log.e("PATTERNMATCHING", "asdasdgdsgasgsadgdfhhfuytrwef");

            Pattern patNumAlphaCost = Pattern.compile(number + words + cost);
            Pattern patAlphaStarCost = Pattern.compile(words + "\\*" + cost);
            Pattern patNumAlpha = Pattern.compile(number + words);
            Pattern patAlpha = Pattern.compile(words);
            Pattern patCost = Pattern.compile(cost);
            Pattern patJustCost = Pattern.compile(justcost);
            Pattern patSpecificCost = Pattern.compile(specificcost);

            Pattern[] patterns = {patAlphaStarCost, patNumAlphaCost};

            Log.e("PATTERNMATCHING", "" + (patNumAlphaCost.matcher("2 DIET PEPSI @ 1.99").matches()));
            Log.e("PATTERNMATCHING", "" + (patterns[0].matcher("2 breadcrumbs @ 5.50").matches()));
            for (int line = 0; line < lines.size(); line++) {
                Item item = new Item(null);
                JsonElement lineData = lines.get(line).getAsJsonObject().get("text");

                if (patAlpha.matcher(lineData.toString()).matches()) {
                    JsonElement nextLineData = lines.get(line + 1).getAsJsonObject().get("text");
                    Log.e("OUT", "" + nextLineData.toString());
                    if (patCost.matcher(nextLineData.toString()).matches()) {
                        System.out.println(lineData.toString() + nextLineData.toString());
                        item.setName(lineData.toString() + nextLineData.toString());
                        this.items.add(item);
                        break;
                    }
                }
                for (Pattern pat : patterns) {

                     if ((pat.matcher(lineData.toString())).matches()) {
                        System.out.println(lineData);
                        item.setName(lineData.toString());
                        this.items.add(item);
                        break;
                    }

                }


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

            String cost = ".*\\d+\\.\\d+.*";
            String words = ".*\\w+[\\w\\s,.-/]*.*";
            String number = ".*\\d+.*";
            String currency = ".*[£$€]+.*";

            Log.e("PATTERNMATCHING", "asdasdgdsgasgsadgdfhhfuytrwef");

            Pattern patNumAlphaCost = Pattern.compile(number + words + cost);
            Pattern patAlphaStarCost = Pattern.compile(words + "\\*" + cost);
            Pattern patNumAlpha = Pattern.compile(number + words);
            Pattern patAlpha = Pattern.compile(words);
            Pattern patCost = Pattern.compile(cost);

            Pattern[] patterns = {patAlphaStarCost, patNumAlpha, patNumAlphaCost};

            Log.e("PATTERNMATCHING", "" + (patNumAlphaCost.matcher("2 DIET PEPSI @ 1.99").matches()));
            Log.e("PATTERNMATCHING", "" + (patterns[0].matcher("2 breadcrumbs @ 5.50").matches()));
            for (int line = 0; line < lines.size(); line++) {
                Item item = new Item(null);
                JsonElement lineData = lines.get(line).getAsJsonObject().get("text");


                for (Pattern pat : patterns) {
                    if ((pat.matcher(lineData.toString())).matches()) {
                        System.out.println(lineData);
                        item.setName(lineData.toString());
                        this.items.add(item);
                        break;
                    } else if (patAlpha.matcher(lineData.toString()).matches()) {
                        JsonElement nextLineData = lines.get(line + 1).getAsJsonObject().get("text");
                        Log.e("OUT", "" + nextLineData.toString());
                        if (patCost.matcher(nextLineData.toString()).matches()) {
                            System.out.println(lineData.toString() + nextLineData.toString());
                            item.setName(lineData.toString() + nextLineData.toString());
                            this.items.add(item);
                            break;
                        }

                    }

                }


            }
        }
    }
}
