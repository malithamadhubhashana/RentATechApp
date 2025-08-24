package com.example.techarent;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.techarent.data.DataManager;
import com.example.techarent.model.TechDevice;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorManager.SensorListener {

    private EditText etSearch;
    private GridLayout gridCategories;
    private DataManager dataManager;
    private DatabaseHelper dbHelper;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_simple);
        
        // Set up toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        // Initialize database helper
        dbHelper = new DatabaseHelper(this);
        
        // Initialize sensor manager
        sensorManager = new SensorManager(this, this);
        
        // Check if user is logged in
        if (!dbHelper.isUserLoggedIn()) {
            // Redirect to welcome screen if not logged in
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        dataManager = DataManager.getInstance();
        initializeViews();
        setupSearchFunctionality();
        setupCategoryClickListeners();
        setupFeaturedItems();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_add_item) {
            Intent intent = new Intent(this, AddItemActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_map) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private void logout() {
        // Clear user session using DatabaseHelper
        dbHelper.logout();
        
        // Show logout message
        Toast.makeText(this, "Logged out successfully", android.widget.Toast.LENGTH_SHORT).show();
        
        // Redirect to welcome activity
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void initializeViews() {
        etSearch = findViewById(R.id.etSearch);
    }

    private void setupSearchFunctionality() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    performSearch(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void performSearch(String query) {
        List<TechDevice> searchResults = dataManager.searchDevices(query);
        if (searchResults.isEmpty()) {
            Toast.makeText(this, "No devices found for: " + query, Toast.LENGTH_SHORT).show();
        } else {
            // For now, just show a toast with results count
            // In a full implementation, this would navigate to search results
            Toast.makeText(this, "Found " + searchResults.size() + " devices", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupCategoryClickListeners() {
        // Get all category cards from the GridLayout
        View rootView = findViewById(android.R.id.content);
        setupCategoryClick(rootView, "Laptops");
        setupCategoryClick(rootView, "Smartphones");
        setupCategoryClick(rootView, "Tablets");
        setupCategoryClick(rootView, "Cameras");
        setupCategoryClick(rootView, "Gaming");
        setupCategoryClick(rootView, "Audio");
    }

    private void setupCategoryClick(View parent, String category) {
        // Find category cards and set click listeners
        // This is a simplified approach - in a real app, you'd use proper view IDs
        parent.setOnTouchListener((v, event) -> {
            // Handle category selection
            navigateToBrowse(category);
            return false;
        });
    }

    private void setupFeaturedItems() {
        // Setup click listeners for featured item "Rent Now" buttons
        // This would typically involve finding the buttons by ID and setting click listeners
        View rootView = findViewById(android.R.id.content);
        
        // For demonstration, we'll show a toast when any rent button is clicked
        rootView.post(() -> {
            // In a real implementation, you'd find actual button views and set click listeners
            Toast.makeText(this, "Featured items loaded successfully", Toast.LENGTH_SHORT).show();
        });
    }

    private void navigateToBrowse(String category) {
        Intent intent = new Intent(this, BrowseActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    public void onCategoryClick(View view) {
        // This method can be called from XML android:onClick
        String category = "All";
        
        // Get the category from the view tag
        if (view.getTag() != null) {
            category = view.getTag().toString();
        }
        
        navigateToBrowse(category);
    }

    public void onRentNowClick(View view) {
        // Handle "Rent Now" button clicks from featured items
        String deviceId = "1"; // Default device ID
        
        // Get device ID from view tag if available
        if (view.getTag() != null) {
            deviceId = view.getTag().toString();
        }
        
        Toast.makeText(this, "Opening item details...", Toast.LENGTH_SHORT).show();
        
        Intent intent = new Intent(this, ItemDetailsActivity.class);
        intent.putExtra("deviceId", deviceId);
        startActivity(intent);
    }

    public void onViewAllClick(View view) {
        // Handle "View All" click in featured section
        navigateToBrowse("All");
    }

    public void onMyRentalsClick(View view) {
        // Navigate to My Rentals screen
        Intent intent = new Intent(this, MyRentalsActivity.class);
        startActivity(intent);
    }
    
    // Sensor lifecycle methods
    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensorManager.startMonitoring();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.stopMonitoring();
        }
    }
    
    // SensorManager.SensorListener implementation
    @Override
    public void onBatteryLevelChanged(int level) {
        // Handle battery level changes
        if (level < 20) {
            Toast.makeText(this, "Low battery: " + level + "%", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onDeviceShaken() {
        // Handle device shake - could refresh content or show shortcuts
        Toast.makeText(this, "Device shaken! Refreshing content...", Toast.LENGTH_SHORT).show();
        // You could refresh the featured items or show search suggestions
    }
    
    @Override
    public void onDeviceDropped() {
        // Handle potential device drop - safety feature
        Toast.makeText(this, "Device movement detected! Please handle with care.", Toast.LENGTH_LONG).show();
    }
}