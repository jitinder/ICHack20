package com.kill.bill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SelectUsers extends AppCompatActivity {

    ArrayList<String> selectedUsers;
    ArrayList<Item> scannedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_users);

        ListView usersLV = (ListView) findViewById(R.id.users_list);

        ArrayList<String> users = new ArrayList<>();
        users.add("Brython");
        users.add("Chris");
        users.add("Cristina");
        users.add("Sidak");
        users.add("Tudor");

        selectedUsers = new ArrayList<>();

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        scannedItems = (ArrayList<Item>) args.getSerializable("ARRAYLIST");

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
        usersLV.setAdapter(itemsAdapter);

        usersLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String name = itemsAdapter.getItem(position);
                if(selectedUsers.contains(name)){
                    selectedUsers.remove(name);
                    view.setBackgroundColor(0x00000000);
                } else {
                    selectedUsers.add(name);
                    view.setBackgroundColor(Color.parseColor("#9BFFE2"));
                }
            }
        });

        Button doneButton = findViewById(R.id.selected_users);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ItemSelector.class);
                intent.putStringArrayListExtra("users", selectedUsers);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)scannedItems);
                intent.putExtra("BUNDLE", args);
                startActivity(intent);
            }
        });
    }
}
