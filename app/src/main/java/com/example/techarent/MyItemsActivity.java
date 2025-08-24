package com.example.techarent;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Activity to display user's listed items for rent
 */
public class MyItemsActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private TextView tvEmptyState;
    private DatabaseHelper dbHelper;
    private MyItemsAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);
        
        // Apply theme
        ThemeHelper.applyTheme(this);
        
        // Set up toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Listed Items");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        initializeViews();
        setupRecyclerView();
        loadUserItems();
    }
    
    private void initializeViews() {
        recyclerView = findViewById(R.id.rv_my_items);
        tvEmptyState = findViewById(R.id.tv_empty_state);
        dbHelper = new DatabaseHelper(this);
        
        // Set up "Add Your First Item" button
        findViewById(R.id.btn_add_item).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddItemActivity.class);
            startActivity(intent);
        });
    }
    
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyItemsAdapter(this, null);
        recyclerView.setAdapter(adapter);
    }
    
    private void loadUserItems() {
        String userEmail = dbHelper.getLoggedInUserEmail();
        
        if (userEmail == null) {
            Toast.makeText(this, "Please log in to view your items", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Get user's listed items from database
        Cursor cursor = dbHelper.getUserListedItems(userEmail);
        
        if (cursor == null || cursor.getCount() == 0) {
            showEmptyState();
        } else {
            showItems();
            adapter.swapCursor(cursor);
        }
    }
    
    private void showEmptyState() {
        recyclerView.setVisibility(android.view.View.GONE);
        tvEmptyState.setVisibility(android.view.View.VISIBLE);
        tvEmptyState.setText("You haven't listed any items yet.\n\nTap the + button on the main screen to add your first item!");
    }
    
    private void showItems() {
        recyclerView.setVisibility(android.view.View.VISIBLE);
        tvEmptyState.setVisibility(android.view.View.GONE);
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh items when returning to this activity
        loadUserItems();
    }
}
