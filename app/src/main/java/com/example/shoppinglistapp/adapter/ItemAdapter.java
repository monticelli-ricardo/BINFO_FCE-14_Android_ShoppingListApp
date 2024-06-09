package com.example.shoppinglistapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglistapp.R;
import com.example.shoppinglistapp.model.Item;

import java.util.Objects;

// Custom adapter extending the ListAdapter used with DiffUtil.ItemCallback utility class to improve
// the performance of RecyclerView (binding items to views) by updating only the necessary parts of the dataset,
// rather than refreshing the entire dataset.
public class ItemAdapter extends ListAdapter<Item, ItemAdapter.ItemViewHolder> {

    // Variables
    private final Context context;
    private final OnItemClickListener listener;

    // Adapter Interface for Item click listener
    public interface OnItemClickListener {
        void onItemClick(Item item, int position); // to edit item
        void onItemLongClick(Item item, int position); // To delete item
        void onItemBoughtChecked(Item item, boolean isChecked); // To mark as bought
    }

    // ItemAdapter Constructor method
    public ItemAdapter(Context context, OnItemClickListener listener) {
        super(DIFF_CALLBACK); // DiffUtil.ItemCallback is passed to the ListAdapter superclass
        this.context = context;
        this.listener = listener;
    }

    // Utility class in Android that helps calculate the differences between two lists and update only the items that have changed.
    private static final DiffUtil.ItemCallback<Item> DIFF_CALLBACK = new DiffUtil.ItemCallback<Item>() {

        // Method to check if two items are the same based on a unique identifier
        @Override
        public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            // Compare unique IDs if you have them or use a unique field
            return oldItem.getItemId() == newItem.getItemId();
        }

        // Method that checks if the content of two items is the same by comparing their fields.
        @Override
        public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            // Compare all the fields of the items
            //return oldItem.getItemTitle().equals(newItem.getItemTitle()) && oldItem.getItemDescription().equals(newItem.getItemDescription());
            return Objects.equals(oldItem, newItem);
        }
    };

    // Method to inflate the item layout and create a new ItemViewHolder.
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view, listener);
    }

    // Method responsible for binding data to the ViewHolder and updating the UI of the item view
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = getItem(position);
        if (item != null) {
            holder.bind(item);
        }
    }

    // Class that holds the views for each item and binds the data to these views. It also handles click event(s).
    class ItemViewHolder extends RecyclerView.ViewHolder {

        // ViewHolder Variables
        private final TextView titleTextView;
        private final TextView descriptionTextView;
        private final TextView quantityTextView;
        private final CheckBox boughtCheckBox;

        // ViewHolder Constructor, setting Listeners
        public ItemViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.itemTitle);
            descriptionTextView = itemView.findViewById(R.id.itemShortDescription);
            quantityTextView = itemView.findViewById(R.id.itemQuantity);
            boughtCheckBox = itemView.findViewById(R.id.itemBought);

            // Listener for click event, using lambda expression
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                // Fetches the corresponding Item object
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position), position);
                }
            });

            // Listener for long-click event, using lambda expression
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemLongClick(getItem(position), position);
                }
                return true;
            });

            // Listener for checkbox click event, using lambda expression
            boughtCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Item item = getItem(position);
                    item.setItemBought(isChecked);
                    listener.onItemBoughtChecked(item, isChecked);
                }
            });
        }

        // Method to bind data to the item view
        public void bind(Item item) {
            titleTextView.setText(item.getItemTitle());
            descriptionTextView.setText(item.getItemDescription());
            quantityTextView.setText(String.valueOf(item.getItemQuantity()));
            boughtCheckBox.setChecked(item.isItemBought());
        }
    }
}
