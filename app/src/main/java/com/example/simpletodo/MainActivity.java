package com.example.simpletodo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    List<String> items;

    ImageButton btnAdd;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        btnAdd = findViewById(R.id.btnAdd);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position, View view, Button done, Button edit, Button delete) {
                Log.d(TAG, "Single click at position " + position);
                Log.i(TAG, "Checkbox clicked!");
                boolean checked = ((CheckBox) view).isChecked();

                // make buttons visible when checked
                if (checked) {
                    done.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                }
                else {
                    done.setVisibility(View.INVISIBLE);
                    edit.setVisibility(View.INVISIBLE);
                    delete.setVisibility(View.INVISIBLE);
                }
            }
        };

        ItemsAdapter.OnDoneClickListener onDoneClickListener = new ItemsAdapter.OnDoneClickListener() {
            @Override
            public void onDoneClicked(int position) {
                remove(position);
                Toast.makeText(getApplicationContext(), "Task completed!", Toast.LENGTH_SHORT).show();
            }
        };

        ItemsAdapter.OnEditClickListener onEditClickListener = new ItemsAdapter.OnEditClickListener() {
            @Override
            public void onEditClicked(int position, String text) {
                onEditClick(position, text);
            }
        };

        ItemsAdapter.OnDeleteClickListener onDeleteClickListener = new ItemsAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClicked(int position) {
                remove(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
            }
        };

        itemsAdapter = new ItemsAdapter(items, onClickListener, onDoneClickListener, onEditClickListener, onDeleteClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager( this));



        // on add button click, pop up dialog
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClick();
            }
        });
    }

    private void onAddClick() {
        // build an alert dialog & prompt user to input new task
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View addView = getLayoutInflater().inflate(R.layout.fragment_add_dialog, null);
        builder.setView(addView);

        final EditText edtTask = addView.findViewById(R.id.edtTask);

        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "Added!");
                // add text in field as task
                String task = edtTask.getText().toString();
                // Add item to the model
                items.add(task);
                // Notify the adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Canceled!", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onEditClick(final int position, String text) {
        // build an alert dialog & prompt user to change current text
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View editView = getLayoutInflater().inflate(R.layout.fragment_edit_dialog, null);
        builder.setView(editView);

        final EditText edtTask = editView.findViewById(R.id.edtTask);
        edtTask.setText(text);

        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("MainAct", "Added!");
                // add text in field as task
                String task = edtTask.getText().toString();
                // Edit item
                items.set(position, task);
                // notify the adapter
                itemsAdapter.notifyItemChanged(position);
                // persist the changes
                saveItems();
                Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Canceled!", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void remove(int position) {
        // Delete the item from the model
        items.remove(position);
        // Notify the adapter
        itemsAdapter.notifyItemRemoved(position);
        saveItems();
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // This function will load items by reading every line of the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e(TAG, "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    // This function saves items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e(TAG, "Error writing items", e);
        }
    }
}
