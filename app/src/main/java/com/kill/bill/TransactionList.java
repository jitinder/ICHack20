package com.kill.bill;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TransactionList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_transaction_list);
        ListView listView = (ListView) findViewById(R.id.transactionListView);
        String[] listValues = {"Domino's - 02/07/2020","Nando's - 01/23/2020"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_list_item, R.id.list_item, listValues);
        listView.setAdapter(arrayAdapter);
    }
}
