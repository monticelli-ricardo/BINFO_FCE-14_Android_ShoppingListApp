package com.example.shoppinglistapp.fragments;

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
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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

    // Declare the binding variable
    private FragmentHomeBinding binding;
    // Getter for the binding instance with non-null assertion
    private FragmentHomeBinding getBinding() {
        if (binding == null) { // Not null check
            throw new IllegalStateException("Attempting to access binding while it is null");
        }
        return binding;
    }


    // Fragment implementation
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using view binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot(); // Return the root view of the binding
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
    // Handle item click
    @Override
    public void onItemClick(Item item, int position) {
        // Navigate to EditItemFragment
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_homeFragment_to_editItemFragment, bundle);
    }
    // Handle long item click
    @Override
    public void onItemLongClick(Item item, int position) {
        // TO-DO later
    }


    // Menu Provider implementation

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        MenuProvider.super.onPrepareMenu(menu);
    }

    // Initialize the app menu
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        // Remove exiting menu
        menu.clear();
        // Inflate new menu
        menuInflater.inflate(R.menu.home_menu, menu);

        // Initialize the searchView from the menu item
        MenuItem menuSearch = menu.findItem(R.id.searchMenu);
        View searchViewActionView = menuSearch.getActionView();
        // Null Check for SearchView
        if (searchViewActionView instanceof SearchView) {
            SearchView searchView = (SearchView) searchViewActionView;
            // Disable submit button
            searchView.setSubmitButtonEnabled(false);
            // Set OnQueryTextListener to handle search queries
            searchView.setOnQueryTextListener(HomeFragment.this);
        }
    }


    // Handling Menu click/selection on element
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        // Do nothing
        return false;
    }

    @Override
    public void onMenuClosed(@NonNull Menu menu) {
        MenuProvider.super.onMenuClosed(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // Do nothing
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText != null){
            searchItem(newText);
        }
        return true;
    }

    // Custom Method to search Item based on string
    private void searchItem(String query){
        viewModel.searchItem(query).observe(getViewLifecycleOwner(), items -> itemAdapter.submitList(items));
    }

    // Custom Method to update the UI
    private void updateHomeUI(List<Item> itemList) {
        if (itemList != null) {
            if (itemList.isEmpty()) {
                binding.emptyListImage.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            } else {
                binding.emptyListImage.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    // Custom Method to set up recyclerViewModel
    private void setupRecyclerViewModel(){
        // Create an instance of the adapter and pass the listener
        itemAdapter = new ItemAdapter(getContext(), this);

        // Initialize HomeRecyclerView
        RecyclerView homeRecyclerView = binding.recyclerView;
        // Set up RecyclerView with the adapter
        homeRecyclerView.setAdapter(itemAdapter);
        // Set RecyclerView fixed size to true

        // Display all items in the recycler view
            // Get the application context
            Application application = requireActivity().getApplication();

            // Initialize ViewModel using the ViewModelFactory
            ItemViewModelFactory factory = new ItemViewModelFactory(application);
            viewModel = new ViewModelProvider(this, factory).get(ItemViewModel.class);
            // Use the ViewModel to observe the LiveData for the itemList changes
            viewModel.getItemList().observe(getViewLifecycleOwner(), items -> {
                itemAdapter.submitList(items);
                updateHomeUI(items); // Update the UI based on the items
            });

    }
}
