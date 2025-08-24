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
        
        // Initialize database helper
        dbHelper = new DatabaseHelper(this);
        
        // Get current user ID from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("TechARent", MODE_PRIVATE);
        currentUserId = prefs.getInt("userId", -1);
        
        // Check if user is logged in
        if (currentUserId == -1) {
            // User not logged in, redirect to login
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
    }
    
    private void loadUserData() {
        // Get user information from database
        Cursor userCursor = dbHelper.getUserById(currentUserId);
        
        if (userCursor != null && userCursor.moveToFirst()) {
            String name = userCursor.getString(userCursor.getColumnIndexOrThrow("name"));
            String email = userCursor.getString(userCursor.getColumnIndexOrThrow("email"));
            String phone = userCursor.getString(userCursor.getColumnIndexOrThrow("phone"));
            
            tvUserName.setText(name);
            tvUserEmail.setText(email);
            tvUserPhone.setText(phone != null && !phone.isEmpty() ? phone : "Not provided");
            
            userCursor.close();
        }
        
        // Get user's item count
        int itemsCount = dbHelper.getUserListingsCount(currentUserId);
        tvItemsCount.setText(String.valueOf(itemsCount));
        
        // Get user's rental count (bookings made by this user)
        int rentalsCount = dbHelper.getUserBookingsCount(currentUserId);
        tvRentalsCount.setText(String.valueOf(rentalsCount));
    }
    
    private void setClickListeners() {
        // Edit Profile
        layoutEditProfile.setOnClickListener(v -> {
            // TODO: Implement edit profile functionality
            Toast.makeText(this, "Edit profile feature coming soon!", Toast.LENGTH_SHORT).show();
        });
        
        // My Items
        layoutMyItems.setOnClickListener(v -> {
            // Open activity to show user's listed items
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("show_my_items", true);
            startActivity(intent);
        });
        
        // Settings
        layoutSettings.setOnClickListener(v -> {
            // TODO: Implement settings functionality
            Toast.makeText(this, "Settings feature coming soon!", Toast.LENGTH_SHORT).show();
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
        // Clear user session
        SharedPreferences prefs = getSharedPreferences("TechARent", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        
        // Show logout message
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        
        // Redirect to login activity
        Intent intent = new Intent(this, LoginActivity.class);
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
}
