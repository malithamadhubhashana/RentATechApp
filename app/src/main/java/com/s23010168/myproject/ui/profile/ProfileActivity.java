package com.s23010168.myproject.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.s23010168.myproject.R;
import com.s23010168.myproject.data.repository.RentalRepository;
import com.s23010168.myproject.models.User;

public class ProfileActivity extends AppCompatActivity {

    private ImageView ivProfileImage, ivBack;
    private TextView tvName, tvEmail, tvUserType, tvListingsCount, tvRating, tvBio;
    private MaterialButton btnEditProfile, btnSwitchRole, btnLogout;
    
    private RentalRepository repository;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        repository = new RentalRepository(this);
        
        initViews();
        setupClickListeners();
        loadUserProfile();
    }

    private void initViews() {
        ivProfileImage = findViewById(R.id.iv_profile_image);
        ivBack = findViewById(R.id.iv_back);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        tvUserType = findViewById(R.id.tv_user_type);
        tvListingsCount = findViewById(R.id.tv_listings_count);
        tvRating = findViewById(R.id.tv_rating);
        tvBio = findViewById(R.id.tv_bio);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnSwitchRole = findViewById(R.id.btn_switch_role);
        btnLogout = findViewById(R.id.btn_logout);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        btnSwitchRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchUserRole();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void loadUserProfile() {
        // For demo purposes, create a sample user
        createSampleUser();
        
        // In a real app, you would load from database
        // String userId = getCurrentUserId();
        // repository.getUserById(userId).observe(this, user -> {
        //     if (user != null) {
        //         currentUser = user;
        //         displayUserProfile();
        //     }
        // });
    }

    private void createSampleUser() {
        currentUser = new User("1", "John Doe", "john@example.com", User.UserType.OWNER);
        currentUser.setBio("I'm a property owner with 5 years of experience in rental management. I love providing comfortable and clean spaces for my tenants.");
        currentUser.setListingsCount(3);
        currentUser.setRating(4.8f);
        currentUser.setReviewCount(15);
        currentUser.setLocation("New York, NY");
        currentUser.setPhoneNumber("+1 (555) 123-4567");
        
        displayUserProfile();
    }

    private void displayUserProfile() {
        tvName.setText(currentUser.getName());
        tvEmail.setText(currentUser.getEmail());
        tvUserType.setText(currentUser.getUserType().toString());
        tvListingsCount.setText(currentUser.getListingsCount() + " listings");
        tvRating.setText(String.format("%.1f", currentUser.getRating()) + " ★");
        tvBio.setText(currentUser.getBio());
        
        // Show/hide switch role button based on user type
        if (currentUser.getUserType() == User.UserType.OWNER) {
            btnSwitchRole.setText("Switch to Renter");
        } else {
            btnSwitchRole.setText("Switch to Owner");
        }
    }

    private void switchUserRole() {
        User.UserType newRole = (currentUser.getUserType() == User.UserType.OWNER) ? 
            User.UserType.RENTER : User.UserType.OWNER;
        
        currentUser.setUserType(newRole);
        repository.updateUser(currentUser);
        
        // Update UI
        tvUserType.setText(currentUser.getUserType().toString());
        if (currentUser.getUserType() == User.UserType.OWNER) {
            btnSwitchRole.setText("Switch to Renter");
        } else {
            btnSwitchRole.setText("Switch to Owner");
        }
    }

    private void logout() {
        // TODO: Clear user session
        // Navigate to welcome screen
        Intent intent = new Intent(ProfileActivity.this, com.s23010168.myproject.ui.welcome.WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
} 