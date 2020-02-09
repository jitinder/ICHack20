package com.kill.bill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ItemAdapter2 extends ArrayAdapter<Item> {
    public ItemAdapter2(Context context, List<Item> items, boolean showIcon) {
        super(context, 0, items);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Item item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_items2, parent, false);
        }

        TextView itemNameTV = convertView.findViewById(R.id.item_name1);
        TextView itemPriceTV = convertView.findViewById(R.id.item_price1);
        TextView itemQuantTV = convertView.findViewById(R.id.item_quantity1);

        if (item != null) {
            itemNameTV.setText(item.getName());
            itemPriceTV.setText(String.valueOf(item.getPrice()));
            itemQuantTV.setText(String.format(Locale.ENGLISH, "Quantity: %d", item.getQuantity()));
        }

        return convertView;
    }
}
