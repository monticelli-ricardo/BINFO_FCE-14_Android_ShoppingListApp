package com.example.shoppinglistapp.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.shoppinglistapp.model.Item;
import com.example.shoppinglistapp.database.ItemDao;
import com.example.shoppinglistapp.database.ItemDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Java class to manage the database operations defined in ItemDao class and handles threading asynchronously
// Single API to the ViewModel class
public class ItemRepository {

    // Item Repository Variables
    private final ItemDao itemDao;
    private final LiveData<List<Item>> allItems;
    private static ItemRepository instance;

    // Logging tag
    private static final String TAG = "ItemRepository";

    // Variable to run database operations on a background thread,
    // ensuring the main thread is not blocked and the UI remains responsive
    private final ExecutorService executorService;

    // Constructor
    public ItemRepository(Application application) {
        ItemDatabase itemDb = ItemDatabase.getInstance(application);
        itemDao = itemDb.itemDao();
        allItems = itemDao.getAllItems();
        executorService = Executors.newFixedThreadPool(2);
    }

    // Singleton instance getter
    public static synchronized ItemRepository getInstance(Application application) {
        if (instance == null) {
            instance = new ItemRepository(application);
            Log.d(TAG, "New instance of ItemRepository created.");
        }
        return instance;
    }

    // "Implement" ItemDao methods (CRUD queries and others)

    // Async Method for query Insert item
    public void insert(Item item) {
        executorService.execute(() -> itemDao.insert(item));
        Log.d(TAG, "Item inserted: " + item);
    }

    // Async method for query Update item
    public void update(Item item) {
        executorService.execute(() -> itemDao.update(item));
        Log.d(TAG, "Item updated: " + item);
    }

    // Async Method for query Delete item
    public void delete(Item item) {
        executorService.execute(() -> itemDao.delete(item));
        Log.d(TAG, "Item deleted: " + item);
    }

    // Async method for query Get all items
    public LiveData<List<Item>> getAllItems() {
        Log.d(TAG, "Fetching all items");
        return allItems;
    }

    // Async method for query look up item based on "keyword"
    public LiveData<List<Item>> searchItem(String query) {
        Log.d(TAG, "Searching for items like: " + query);
        return itemDao.searchItem(query);
    }

}
