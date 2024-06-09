package com.example.shoppinglistapp.fragments;

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

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shoppinglistapp.R;
import com.example.shoppinglistapp.databinding.FragmentAddItemBinding;
import com.example.shoppinglistapp.model.Item;
import com.example.shoppinglistapp.viewmodel.ItemViewModel;
import com.example.shoppinglistapp.viewmodel.ItemViewModelFactory;

// Class to for UI to save new Item details into the database
public class AddItemFragment extends Fragment implements MenuProvider {


    // Variables
    private View addItemView;
    private ItemViewModel viewModel;
    // The binding variable
    private FragmentAddItemBinding binding;
    // Getter for the binding instance with non-null assertion
    private FragmentAddItemBinding getBinding() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddItemBinding.inflate(inflater, container, false);
        return binding.getRoot(); // Return the root view of the binding
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize MenuHost
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        // Set up the ViewModel
        ItemViewModelFactory factory = new ItemViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(ItemViewModel.class);

        // Floating action button to add new item
        binding.doneItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save item details and return to home page
                saveItemDetails(v);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // cleans up the binding reference to avoid memory leaks
    }

    // Menu Provider Implementation
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        // Remove exiting menu
        menu.clear();
        // Inflate new menu
        menuInflater.inflate(R.menu.menu_add_item, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.actionHome) {
            // Handle navigation back to the home fragment
            NavController navController = Navigation.findNavController(requireView());
            navController.navigateUp();
            return true;
        }
        return false;
    }

    // Custom Method to save Item details
    private void saveItemDetails(View view) {
        String itemTitle = binding.addItemTitle.getText().toString().trim();
        String itemDesc = binding.addItemDesc.getText().toString().trim();
        String itemQuantityString = binding.addItemQuantity.getText().toString().trim();
        boolean itemBought = false;

        int itemQuantity;
        try {
            itemQuantity = Integer.parseInt(itemQuantityString);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid number for the item quantity.", Toast.LENGTH_SHORT).show();
            return;
        }


        // Validate user's input is complete
        if (!itemTitle.isEmpty() && !itemDesc.isEmpty() && !itemQuantityString.isEmpty()) {
            // Create a new item with the recently input information
            Item newItem = Item.createItem(itemTitle, itemDesc, itemQuantity, itemBought);
            // Add new item into the Database via the ViewModel
            viewModel.insertItem(newItem);

            Toast.makeText(requireContext(), itemTitle + " is saved.", Toast.LENGTH_SHORT).show();
            // Return to Home Fragment
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_addItemFragment_to_homeFragment);
        } else {
            // Notify user that creation is not possible until all fields fill-in
            Toast.makeText(requireContext(), "Please complete all fields.", Toast.LENGTH_SHORT).show();
        }
    }
}
