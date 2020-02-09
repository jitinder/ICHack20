package com.kill.bill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class ItemSelector extends AppCompatActivity {

    List<String> selectedUsers;
    FragmentTransaction ft;
    int userCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selector);

        userCounter = 0;

        selectedUsers = getIntent().getStringArrayListExtra("users");
        if(selectedUsers != null && !selectedUsers.isEmpty()) {
            // Begin the transaction
            ft = getSupportFragmentManager().beginTransaction();
            // Replace the contents of the container with the new fragment
            ft.replace(R.id.user_fragment_container, new ItemSelection(selectedUsers.get(userCounter)));
            // or ft.add(R.id.your_placeholder, new FooFragment());
            // Complete the changes added above
            ft.commit();
        }

        Button next = findViewById(R.id.confirm_user_select);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userCounter + 1 < selectedUsers.size()){
                    userCounter++;
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.user_fragment_container, new ItemSelection(selectedUsers.get(userCounter)));
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(ItemSelector.this, "Reached End of Users", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
