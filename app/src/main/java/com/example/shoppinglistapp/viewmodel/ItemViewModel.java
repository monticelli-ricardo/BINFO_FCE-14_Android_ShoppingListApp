package com.example.shoppinglistapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.shoppinglistapp.model.Item;
import com.example.shoppinglistapp.repository.ItemRepository;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {

    // Variables
    private final ItemRepository itemRepository;
    private final LiveData<List<Item>> itemList;

    // Constructor
    public ItemViewModel(@NonNull Application application, ItemRepository itemRepository) {
        super(application);
        this.itemRepository = itemRepository;
        itemList = itemRepository.getAllItems();
    }

    // `ItemRepository` API implementation
    public LiveData<List<Item>> getItemList() {
        return itemList;
    }

    public void insertItem(Item item) {
        itemRepository.insert(item);
    }

    public void updateItem(Item item) {
        itemRepository.update(item);
    }

    public void deleteItem(Item item) {
        itemRepository.delete(item);
    }

    public LiveData<List<Item>> searchItem(String query) {
        return itemRepository.searchItem(query);
    }


}
