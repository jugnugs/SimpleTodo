package com.example.simpletodo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Responsible for displaying data from the model into a row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnClickListener {
        void onItemClicked(int position, View view, Button done, Button edit, Button delete);
    }

    public interface OnDoneClickListener {
        void onDoneClicked(int position);
    }

    public interface OnEditClickListener {
        void onEditClicked(int position, String text);
    }

    public interface OnDeleteClickListener {
        void onDeleteClicked(int position);
    }

    private static final String TAG = "ItemsAdapter";

    private List<String> items;
    private OnClickListener clickListener;
    private OnDoneClickListener doneClickListener;
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener deleteClickListener;

    ItemsAdapter(List<String> items,
                 OnClickListener clickListener,
                 OnDoneClickListener doneClickListener,
                 OnEditClickListener editClickListener,
                 OnDeleteClickListener deleteClickListener) {
        this.items = items;
        this.clickListener = clickListener;
        this.doneClickListener = doneClickListener;
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use layout inflater to inflate a view

        View todoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        // wrap it inside a View Holder and return it
        return new ViewHolder(todoView);
    }

    // responsible for binding data to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Grab the item at the position
        String item = items.get(position);
        // Bind the item into the specified view holder
        holder.bind(item);
    }

    // Tells the RV how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    // Container to provide easy access to views that represent each row in the list
    class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox btnCheck;
        Button btnDone;
        Button btnEdit;
        Button btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnCheck = itemView.findViewById(R.id.tvCheckBox);
            btnDone = itemView.findViewById(R.id.btnDone);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        // Update the view inside of the view holder with this data
        void bind(String item) {
            btnCheck.setText(item);
            btnCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition(), btnCheck, btnDone, btnDelete, btnEdit);
                }
            });
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Done!");
                    doneClickListener.onDoneClicked(getAdapterPosition());
                }
            });
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Edit");
                    editClickListener.onEditClicked(getAdapterPosition(), btnCheck.getText().toString());
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Delete");
                    deleteClickListener.onDeleteClicked(getAdapterPosition());
                }
            });
        }
    }
}
