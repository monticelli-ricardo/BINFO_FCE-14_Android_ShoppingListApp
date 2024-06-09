package com.example.shoppinglistapp.fragments;

import android.app.AlertDialog;
import android.app.Application;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.shoppinglistapp.R;
import com.example.shoppinglistapp.adapter.ItemAdapter;
import com.example.shoppinglistapp.databinding.FragmentHomeBinding;
import com.example.shoppinglistapp.model.Item;
import com.example.shoppinglistapp.viewmodel.ItemViewModel;
import com.example.shoppinglistapp.viewmodel.ItemViewModelFactory;

import java.util.List;

public class HomeFragment extends Fragment implements ItemAdapter.OnItemClickListener, SearchView.OnQueryTextListener, MenuProvider {

    // Variables
    private ItemAdapter itemAdapter;
    private ItemViewModel viewModel;
    private FragmentHomeBinding binding;
    private ImageView emptyListImage;
    private static final String TAG = "HomeFragment";
    // Getters
    private FragmentHomeBinding getBinding() {
        if (binding == null) {
            throw new IllegalStateException("Attempting to access binding while it is null");
        }
        return binding;
    }

    // Fragments implementation
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment using view binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize MenuHost
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        // Set up the ViewModel
        setupRecyclerViewModel();

        // Floating action button to add new item
        binding.addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the AddItemFragment
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_homeFragment_to_addItemFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // cleans up the binding reference to avoid memory leaks
    }

    // Click event implementation
        @Override
    public void onItemClick(Item item, int position) { // Handle item click
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);
        // Navigate to EditItemFragment
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_homeFragment_to_editItemFragment, bundle);
    }

    @Override
    public void onItemLongClick(Item item, int position) { // Handle long item click
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Item")
                .setMessage("Do you want to proceed?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteItem(item);
                    Toast.makeText(getContext(), item.getItemTitle() + " deleted.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onItemBoughtChecked(Item item, boolean isChecked) { // Handle check box event
        viewModel.updateItem(item);
        if (!isChecked) {
            Toast.makeText(getContext(), "Not enough of " + item.getItemTitle(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), item.getItemTitle() + " bought.", Toast.LENGTH_SHORT).show();
        }

    }

    // Menu implementation

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        MenuProvider.super.onPrepareMenu(menu);
    }

    // Initialize the app menu
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        // Remove existing menu
        menu.clear();
        // Inflate new menu
        menuInflater.inflate(R.menu.home_menu, menu);

        // Initialize the searchView from the menu item
        MenuItem menuSearch = menu.findItem(R.id.searchMenu);
        View searchViewActionView = menuSearch.getActionView();
        // Null Check for SearchView
        if (searchViewActionView instanceof SearchView) {
            SearchView searchView = (SearchView) searchViewActionView;
            searchView.setSubmitButtonEnabled(false); // Disable submit button
            searchView.setOnQueryTextListener(HomeFragment.this); // Set OnQueryTextListener to handle search queries
        }
    }

    // Handling Menu click/selection on element
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onMenuClosed(@NonNull Menu menu) {
        MenuProvider.super.onMenuClosed(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String newText) {
        if (newText != null && !newText.trim().isEmpty()) {
            Log.d(TAG, "onQueryTextSubmit: " + newText);
            searchItem(newText.trim());
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText != null && !newText.trim().isEmpty()) {
            Log.d(TAG, "onQueryTextChange: " + newText);
            searchItem(newText.trim());
        }
        return true;
    }

    // Custom methods

    // Method to search for an item based on user input string
    private void searchItem(String query) {
        // Wrap query in '%' for SQL LIKE query
        String searchQuery = "%" + query + "%";
        viewModel.searchItem(searchQuery).observe(getViewLifecycleOwner(), items -> {
            Log.d(TAG, "Search results: " + items);
            itemAdapter.submitList(items);
        });
    }

    // Method to update the UI with the list of items or empty list image
    private void updateHomeUI(List<Item> itemList) {
        if (itemList != null && !itemList.isEmpty()) {
            binding.emptyListImage.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        } else {
            binding.emptyListImage.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        }
    }

    // Method to setup the RecyclerView and ViewModel
    private void setupRecyclerViewModel() {
        // Create an instance of the adapter and pass the listener
        itemAdapter = new ItemAdapter(getContext(), this);
        // Initialize the layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        // Bind home fragment view to the recycler view
        RecyclerView homeRecyclerView = binding.recyclerView;
        // Set the adapter and layout manager
        homeRecyclerView.setAdapter(itemAdapter);
        homeRecyclerView.setHasFixedSize(true);
        homeRecyclerView.setLayoutManager(layoutManager);
        // Display all items in the recycler view
            // Get the application context
            Application application = requireActivity().getApplication();
            // Initialize the ViewModelFactory with the application context
            ItemViewModelFactory factory = new ItemViewModelFactory(application);
            // Initialize the ViewModel with the factory
            viewModel = new ViewModelProvider(this, factory).get(ItemViewModel.class);
        // Use the ViewModel to observe the LiveData for the itemList changes
            viewModel.getItemList().observe(getViewLifecycleOwner(), items -> {
                // For logging
                Log.d(TAG, "Item list size: " + (items != null ? items.size() : "null"));
                // Submit the list of items to the adapter
                itemAdapter.submitList(items);
                // Update the UI based on the items list
                updateHomeUI(items);
            });
    }

}
