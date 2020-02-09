package com.kill.bill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {
    public ItemAdapter(Context context, List<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_items, parent, false);
        }

        TextView itemNameTV = convertView.findViewById(R.id.item_name);
        TextView itemPriceTV = convertView.findViewById(R.id.item_price);
        TextView itemQuantTV = convertView.findViewById(R.id.item_quantity);
        ImageView editValueIV = convertView.findViewById(R.id.edit_item_info);

        if(item != null) {
            itemNameTV.setText(item.getName());
            itemPriceTV.setText(String.valueOf(item.getPrice()));
            itemQuantTV.setText("Quantity: " + item.getQuantity());
        }

        editValueIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                View dialogView = inflater.inflate(R.layout.edit_item_dialog, null);
                builder.setView(dialogView);

                EditText itemName = (EditText) dialogView.findViewById(R.id.edit_item_name);
                EditText itemPrice = (EditText) dialogView.findViewById(R.id.edit_item_value);
                EditText itemQuant = (EditText) dialogView.findViewById(R.id.edit_item_quant);

                builder.setTitle("Modify Item Value")
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String newName = "";
                                double newPrice = 0.0;
                                int newQuant = 0;
                                if(itemName.getText().toString().equals("")) {
                                    newName = item.getName();
                                } else {
                                    newName = itemName.getText().toString();
                                }
                                if(itemPrice.getText().toString().equals("")) {
                                    newPrice = item.getPrice();
                                } else {
                                    newPrice = Double.parseDouble(itemPrice.getText().toString());
                                }
                                if(itemQuant.getText().toString().equals("")) {
                                    newQuant = item.getQuantity();
                                } else {
                                    newQuant = Integer.parseInt(itemQuant.getText().toString());
                                }
                                getItem(position).setName(newName);
                                getItem(position).setPrice(newPrice);
                                getItem(position).setQuantity(newQuant);

                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Close LOL
                    }
                });

                itemName.setHint(item.getName());
                itemPrice.setHint(String.valueOf(item.getPrice()));
                itemQuant.setHint(String.valueOf(item.getQuantity()));


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return convertView;
    }
}
