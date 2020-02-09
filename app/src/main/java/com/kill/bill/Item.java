package com.kill.bill;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
  private String name;
  private double price;
  private int quantity;

  protected Item(Parcel in) {
    if (in == null) {
      return;
    }

    name = in.readString();
    price = in.readDouble();
    quantity = in.readInt();
  }

  public static final Creator<Item> CREATOR =
      new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
          return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
          return new Item[size];
        }
      };

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public int describeContents() {
    return name.hashCode();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeDouble(price);
    dest.writeInt(quantity);
  }
}
