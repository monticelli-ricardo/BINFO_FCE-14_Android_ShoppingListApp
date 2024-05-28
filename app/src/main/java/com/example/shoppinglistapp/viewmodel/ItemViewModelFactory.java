package com.example.shoppinglistapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.shoppinglistapp.repository.ItemRepository;

// Class to manage ViewModel instantiation based on required parameters (Android Application class, Item Repository)
public class ItemViewModelFactory implements ViewModelProvider.Factory {

    // Variables
    private final ItemRepository repository;
    private final Application application;

    // ItemViewModelFactory Constructor
    public ItemViewModelFactory(Application app, ItemRepository repository) {
        this.application = app;
        this.repository = repository;

    }
    // Custom ViewModelProvider.Factory implementation in Android,
    // to create instances of the ItemViewModel class with custom constructor parameters (Application and ItemRepository),
    // which the default ViewModelProvider.Factory cannot provide.
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ItemViewModel.class)) { // Safe cast check, preventing ClassCastException at runtime.
            @SuppressWarnings("unchecked") // suppress unchecked cast warnings and create ViewModel
            T viewModel = (T) new ItemViewModel(application, repository);
            return viewModel;
        } // If the provided modelClass is not assignable throw exception
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
