package com.example.techarent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    
    private DatabaseHelper dbHelper;
    private int currentUserId;
    
    private TextView tvUserName, tvUserEmail, tvUserPhone;
    private TextView tvItemsCount, tvRentalsCount;
    private LinearLayout layoutEditProfile, layoutMyItems, layoutSettings, layoutHelp;
    private Button btnLogout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        // Setup action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        // Initialize database helper
        dbHelper = new DatabaseHelper(this);
        
        // Check if user is logged in
        if (!dbHelper.isUserLoggedIn()) {
            // User not logged in, redirect to login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        // Get current user email and then find user ID
        String userEmail = dbHelper.getLoggedInUserEmail();
        
        if (userEmail != null) {
            // Get user object from email using existing method
            User currentUser = dbHelper.getUserByEmail(userEmail);
            if (currentUser != null) {
                currentUserId = currentUser.getId();
            } else {
                // User not found, redirect to login
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        } else {
            // No email found, redirect to login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        // Initialize views
        initViews();
        
        // Load user data
        loadUserData();
        
        // Set click listeners
        setClickListeners();
    }
    
    private void initViews() {
        try {
            tvUserName = findViewById(R.id.tv_user_name);
            tvUserEmail = findViewById(R.id.tv_user_email);
            tvUserPhone = findViewById(R.id.tv_user_phone);
            tvItemsCount = findViewById(R.id.tv_items_count);
            tvRentalsCount = findViewById(R.id.tv_rentals_count);
            
            layoutEditProfile = findViewById(R.id.layout_edit_profile);
            layoutMyItems = findViewById(R.id.layout_my_items);
            layoutSettings = findViewById(R.id.layout_settings);
            layoutHelp = findViewById(R.id.layout_help);
            
            btnLogout = findViewById(R.id.btn_logout);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error initializing profile page", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    private void loadUserData() {
        try {
            // Get user information from database
            Cursor userCursor = dbHelper.getUserById(currentUserId);
            
            if (userCursor != null && userCursor.moveToFirst()) {
                String name = userCursor.getString(userCursor.getColumnIndexOrThrow("full_name"));
                String email = userCursor.getString(userCursor.getColumnIndexOrThrow("email"));
                String phone = userCursor.getString(userCursor.getColumnIndexOrThrow("phone"));
                
                tvUserName.setText(name != null ? name : "Unknown");
                tvUserEmail.setText(email != null ? email : "Unknown");
                tvUserPhone.setText(phone != null && !phone.isEmpty() ? phone : "Not provided");
                
                userCursor.close();
            } else {
                // Set default values if user not found
                tvUserName.setText("Unknown User");
                tvUserEmail.setText("Unknown Email");
                tvUserPhone.setText("Not provided");
            }
            
            // Get user's item count
            int itemsCount = dbHelper.getUserListingsCount(currentUserId);
            tvItemsCount.setText(String.valueOf(itemsCount));
            
            // Get user's rental count (bookings made by this user)
            int rentalsCount = dbHelper.getUserBookingsCount(currentUserId);
            tvRentalsCount.setText(String.valueOf(rentalsCount));
        } catch (Exception e) {
            e.printStackTrace();
            // Set default values in case of error
            if (tvUserName != null) tvUserName.setText("Error loading data");
            if (tvUserEmail != null) tvUserEmail.setText("Error loading data");
            if (tvUserPhone != null) tvUserPhone.setText("Error loading data");
            if (tvItemsCount != null) tvItemsCount.setText("0");
            if (tvRentalsCount != null) tvRentalsCount.setText("0");
        }
    }
    
    private void setClickListeners() {
        // Edit Profile
        layoutEditProfile.setOnClickListener(v -> {
            // TODO: Implement edit profile functionality
            Toast.makeText(this, "Edit profile feature coming soon!", Toast.LENGTH_SHORT).show();
        });
        
        // My Items
        layoutMyItems.setOnClickListener(v -> {
            // Open MyItemsActivity to show user's listed items
            Intent intent = new Intent(this, MyItemsActivity.class);
            startActivity(intent);
        });
        
        // Settings
        layoutSettings.setOnClickListener(v -> {
            // Open Settings Activity
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
        
        // Help & Support
        layoutHelp.setOnClickListener(v -> {
            // TODO: Implement help & support functionality
            Toast.makeText(this, "Help & Support feature coming soon!", Toast.LENGTH_SHORT).show();
        });
        
        // Logout
        btnLogout.setOnClickListener(v -> logout());
    }
    
    private void logout() {
        // Clear user session using DatabaseHelper
        dbHelper.logout();
        
        // Show logout message
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        
        // Redirect to welcome activity
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to this activity
        loadUserData();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
