package com.example.shoppinglistapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.shoppinglistapp.model.Item;

// Define the database, including the entities and version
@Database(entities = {Item.class}, version = 1)
public abstract class ItemDatabase extends RoomDatabase {

    // Singleton instance of the database, with multiple threads handle
    private static volatile ItemDatabase instance;

    // Define the DAO to access the database
    public abstract ItemDao itemDao();

    // Method to get the singleton instance of the database with double-checked locking
    public static ItemDatabase getInstance(Context context) {
        if (instance == null) {
            // synchronizes and performs another null check before creating the instance
            synchronized (ItemDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    ItemDatabase.class, "item_db") // Database name: item_db
                            .fallbackToDestructiveMigration() // Handle schema migrations
                            .build();
                }
            }
        }
        return instance;
    }
}

