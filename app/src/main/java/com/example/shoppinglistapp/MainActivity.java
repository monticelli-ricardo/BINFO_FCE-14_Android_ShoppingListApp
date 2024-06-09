package com.example.shoppinglistapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.shoppinglistapp.viewmodel.ItemViewModel;
import com.example.shoppinglistapp.viewmodel.ItemViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private ItemViewModel itemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViewModel();
    }

    private void setupViewModel(){
        ItemViewModelFactory factory = new ItemViewModelFactory(getApplication());
        itemViewModel = new ViewModelProvider(this, factory).get(ItemViewModel.class);
    }
}
