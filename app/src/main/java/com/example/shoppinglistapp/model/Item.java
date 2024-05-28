package com.example.shoppinglistapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

// Item data structure
@Entity(tableName = "items")
public class Item implements Parcelable{
    private String itemTitle;
    private String itemDescription;
    @PrimaryKey(autoGenerate = true)
    private int itemId;

    // Getters and Setters
    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
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
    }

    protected Item(Parcel in) {
        itemTitle = in.readString();
        itemDescription = in.readString();
        itemId = in.readInt();
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
