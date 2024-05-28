package com.example.shoppinglistapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shoppinglistapp.model.Item;

import java.util.List;

// Interface for defining the Shopping List database operations
@Dao
public interface ItemDao {

    // Database query to add a new item,
    // in the event of primary conflict replace the data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Item item);

    // Database query to update item
    @Update
    public void update(Item item);

    // Database query to delete item
    @Delete
    public void delete(Item item);

    // Database query to get all items ordered by ID
    @Query("SELECT * FROM items ORDER BY itemId DESC")
    public LiveData<List<Item>> getAllItems();

    // Database query to search for item based on title or description
    @Query("SELECT * FROM items WHERE itemTitle LIKE :query OR itemDescription LIKE :query")
    public LiveData<List<Item>> searchItem(String query);

}
