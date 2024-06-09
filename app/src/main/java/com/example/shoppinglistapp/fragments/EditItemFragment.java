package com.example.shoppinglistapp.fragments;

import android.app.AlertDialog;
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
import com.example.shoppinglistapp.databinding.FragmentEditItemBinding;
import com.example.shoppinglistapp.model.Item;
import com.example.shoppinglistapp.viewmodel.ItemViewModel;
import com.example.shoppinglistapp.viewmodel.ItemViewModelFactory;

// Class for UI to update and delete item from the database
public class EditItemFragment extends Fragment implements MenuProvider {

    // Variables
    private ItemViewModel viewModel;
    private Item currentItem;

    // Set up binding
    private FragmentEditItemBinding binding;
    private FragmentEditItemBinding getBinding() {
        if (binding == null) { // Not null check
            throw new IllegalStateException("Attempting to access binding while it is null");
        }
        return binding;
    }

    // Fragment implementation
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the item from the arguments
        if (getArguments() != null) {
            currentItem = getArguments().getParcelable("item");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
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

        // NotNull check and bind current Item data into Edit View components
        if (currentItem != null) {
            binding.editItemTitle.setText(currentItem.getItemTitle());
            binding.editItemDesc.setText(currentItem.getItemDescription());
            binding.editItemQuantity.setText(String.valueOf(currentItem.getItemQuantity())); // make sure to pass int to string
        }

        // Floating action button to add new item
        binding.saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save item details and return to home page
                updateItemDetails(v);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // cleans up the binding reference to avoid memory leaks
    }

    // MenuProvider implementation
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        // Remove exiting menu
        menu.clear();
        // Inflate new menu
        menuInflater.inflate(R.menu.menu_edit_item, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.actionDelete) {
            deleteItem(getView());
            return true;
        } else if (itemId == R.id.actionHome) {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_editItemFragment_to_homeFragment);
            return true;
        }
        return false;
    }

    // Custom Method to save Item details
    private void updateItemDetails(View view){

        // Bind item data (title, description, etc.)
        String itemTitle = binding.editItemTitle.getText().toString().trim();
        String itemDesc = binding.editItemDesc.getText().toString().trim();
        String itemQuantity = binding.editItemQuantity.getText().toString().trim();

        // Validate user's input is complete
        if(!itemTitle.isEmpty()){

            // set item new details
            currentItem.setItemTitle(itemTitle);
            currentItem.setItemDescription(itemDesc);
            currentItem.setItemQuantity(Integer.parseInt(itemQuantity));

            // Update item data in the Database via the ViewModel
            viewModel.updateItem(currentItem);

            // Notify the user about the insert
            Toast.makeText(getContext(),itemTitle + " is updated.", Toast.LENGTH_SHORT).show();

            // Return to Home Fragment
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_editItemFragment_to_homeFragment);

        } else {
            //
            Toast.makeText(getContext(), "Item title is a mandatory field. Please fill in.", Toast.LENGTH_SHORT).show();
        }
    }

    // Custom method to delete item
    private void deleteItem(View view) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Item")
                .setMessage("Do you want to proceed?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteItem(currentItem);
                    Toast.makeText(getContext(), "Item deleted.", Toast.LENGTH_SHORT).show();
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_editItemFragment_to_homeFragment);
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}