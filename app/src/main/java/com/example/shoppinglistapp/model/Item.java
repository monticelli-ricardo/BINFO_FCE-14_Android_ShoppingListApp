package com.example.shoppinglistapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

// Item data structure
@Entity(tableName = "items")
public class Item implements Parcelable{
    private String itemTitle;
    private String itemDescription;
    private int itemQuantity;
    private boolean itemBought;
    private String itemUnit;
    @PrimaryKey(autoGenerate = true)
    private int itemId;

    // Getters and Setters

    // Protected setter for Room to use
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    public int getItemId() {
        return itemId;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }


    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public boolean isItemBought() {
        return itemBought;
    }

    public void setItemBought(boolean itemBought) {
        this.itemBought = itemBought;
    }
    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }
    // End of getters and setters

    // Constructor
    // Default constructor required by Room
    public Item() {}

    @Ignore
    public Item(String itemTitle, String itemDescription, int itemQuantity, boolean itemBought, String itemUnit) {
        this.itemTitle = itemTitle;
        this.itemDescription = itemDescription;
        this.itemQuantity = itemQuantity;
        this.itemBought = itemBought;
        this.itemUnit = itemUnit;
    }

    // Public static method to create new Item instances
    public static Item createItem(String title, String description, int itemQuantity, boolean itemBought, String itemUnit) {
        return new Item(title, description, itemQuantity, itemBought, itemUnit);
    }

    // Parcelable implementation for efficient data transfer among components.
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(itemTitle);
        dest.writeString(itemDescription);
        dest.writeInt(itemId);
        dest.writeInt(itemQuantity);
        dest.writeByte((byte) (itemBought ? 1 : 0));
        dest.writeString(itemUnit);
    }

    protected Item(Parcel in) {
        itemTitle = in.readString();
        itemDescription = in.readString();
        itemId = in.readInt();
        itemBought = in.readByte() != 0;
        itemQuantity = in.readInt();
        itemUnit = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };


}
