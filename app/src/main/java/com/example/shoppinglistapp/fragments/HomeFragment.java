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

    private ItemAdapter itemAdapter;
    private ItemViewModel viewModel;
    private FragmentHomeBinding binding;

    private FragmentHomeBinding getBinding() {
        if (binding == null) {
            throw new IllegalStateException("Attempting to access binding while it is null");
        }
        return binding;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        setupRecyclerViewModel();

        binding.addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_homeFragment_to_addItemFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Item item, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_homeFragment_to_editItemFragment, bundle);
    }

    @Override
    public void onItemLongClick(Item item, int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Item")
                .setMessage("Do you want to proceed?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteItem(item);
                    Toast.makeText(getContext(), "Item deleted.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        MenuProvider.super.onPrepareMenu(menu);
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.home_menu, menu);

        MenuItem menuSearch = menu.findItem(R.id.searchMenu);
        View searchViewActionView = menuSearch.getActionView();
        if (searchViewActionView instanceof SearchView) {
            SearchView searchView = (SearchView) searchViewActionView;
            searchView.setSubmitButtonEnabled(false);
            searchView.setOnQueryTextListener(HomeFragment.this);
        }
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onMenuClosed(@NonNull Menu menu) {
        MenuProvider.super.onMenuClosed(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText != null) {
            searchItem(newText);
        }
        return true;
    }

    private void searchItem(String query) {
        viewModel.searchItem(query).observe(getViewLifecycleOwner(), items -> itemAdapter.submitList(items));
    }

    private void updateHomeUI(List<Item> itemList) {
        if (itemList != null && !itemList.isEmpty()) {
            binding.emptyListImage.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
            itemAdapter.submitList(itemList);
        } else {
            binding.emptyListImage.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerViewModel() {
        itemAdapter = new ItemAdapter(getContext(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        RecyclerView homeRecyclerView = binding.recyclerView;
        homeRecyclerView.setAdapter(itemAdapter);
        homeRecyclerView.setHasFixedSize(true);
        homeRecyclerView.setLayoutManager(layoutManager);

        Application application = requireActivity().getApplication();

        ItemViewModelFactory factory = new ItemViewModelFactory(application);
        viewModel = new ViewModelProvider(this, factory).get(ItemViewModel.class);
        viewModel.getItemList().observe(getViewLifecycleOwner(), items -> {
            Log.d("HomeFragment", "Item list size: " + (items != null ? items.size() : "null"));
            binding.recyclerView.setVisibility(View.VISIBLE);
            itemAdapter.submitList(items);
        });
    }

}
