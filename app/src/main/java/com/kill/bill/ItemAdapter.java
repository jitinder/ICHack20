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

public class ItemAdapter extends ArrayAdapter<Item> {
    boolean showIcon = true;
  public ItemAdapter(Context context, List<Item> items, boolean showIcon) {
    super(context, 0, items);
  }

  @Override
  @NonNull
  public View getView(int position, View convertView, @NonNull ViewGroup parent) {
    Item item = getItem(position);

    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_items, parent, false);
    }

    TextView itemNameTV = convertView.findViewById(R.id.item_name);
    TextView itemPriceTV = convertView.findViewById(R.id.item_price);
    TextView itemQuantTV = convertView.findViewById(R.id.item_quantity);
    ImageView editValueIV = convertView.findViewById(R.id.edit_item_info);
    if(!showIcon){
        editValueIV.setVisibility(View.INVISIBLE);
    }

    if (item != null) {
      itemNameTV.setText(item.getName());
      itemPriceTV.setText(String.valueOf(item.getPrice()));
      itemQuantTV.setText(String.format(Locale.ENGLISH, "Quantity: %d", item.getQuantity()));
    }

    editValueIV.setOnClickListener(
        view -> {
          AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
          LayoutInflater inflater = LayoutInflater.from(view.getContext());
          View dialogView = inflater.inflate(R.layout.edit_item_dialog, parent);
          builder.setView(dialogView);

          EditText itemName = dialogView.findViewById(R.id.edit_item_name);
          EditText itemPrice = dialogView.findViewById(R.id.edit_item_value);
          EditText itemQuant = dialogView.findViewById(R.id.edit_item_quant);

          builder
              .setTitle("Modify Item Value")
              .setPositiveButton(
                  "Save",
                  (dialogInterface, i) -> {
                    String newName;
                    int newQuant;
                    double newPrice;

                    if (itemName.getText().toString().equals("") && item != null) {
                      newName = item.getName();
                    } else {
                      newName = itemName.getText().toString();
                    }
                    if (itemPrice.getText().toString().equals("") && item != null) {
                      newPrice = item.getPrice();
                    } else {
                      newPrice = Double.parseDouble(itemPrice.getText().toString());
                    }
                    if (itemQuant.getText().toString().equals("") && item != null) {
                      newQuant = item.getQuantity();
                    } else {
                      newQuant = Integer.parseInt(itemQuant.getText().toString());
                    }

                    Objects.requireNonNull(getItem(position)).setName(newName);
                    Objects.requireNonNull(getItem(position)).setPrice(newPrice);
                    Objects.requireNonNull(getItem(position)).setQuantity(newQuant);

                    notifyDataSetChanged();
                  })
              .setNegativeButton(
                  "Cancel",
                  (dialogInterface, i) -> {
                    // Close LOL
                  });

          itemName.setHint(Objects.requireNonNull(item).getName());
          itemPrice.setHint(String.valueOf(item.getPrice()));
          itemQuant.setHint(String.valueOf(item.getQuantity()));

          AlertDialog alertDialog = builder.create();
          alertDialog.show();
        });

    convertView.setOnClickListener(
        o -> {
          Intent intent = new Intent(parent.getContext(), TransactionDetails.class);
          intent.putExtra("item", item);
          parent.getContext().startActivity(intent);
        });

    return convertView;
  }
}
