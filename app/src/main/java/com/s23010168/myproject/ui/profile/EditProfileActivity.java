package com.s23010168.myproject.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.s23010168.myproject.R;
import com.s23010168.myproject.data.repository.RentalRepository;
import com.s23010168.myproject.models.User;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView ivBack, ivProfileImage;
    private EditText etName, etEmail, etPhone, etLocation, etBio;
    private Spinner spinnerUserType;
    private MaterialButton btnSave, btnCancel;
    private TextView tvTitle;
    
    private RentalRepository repository;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        repository = new RentalRepository(this);
        
        initViews();
        setupClickListeners();
        loadUserData();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        ivProfileImage = findViewById(R.id.iv_profile_image);
        tvTitle = findViewById(R.id.tv_title);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etLocation = findViewById(R.id.et_location);
        etBio = findViewById(R.id.et_bio);
        spinnerUserType = findViewById(R.id.spinner_user_type);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement image picker
                // selectProfileImage();
            }
        });
    }

    private void loadUserData() {
        // For demo purposes, create a sample user
        createSampleUser();
        
        // In a real app, you would load from database
        // String userId = getCurrentUserId();
        // repository.getUserById(userId).observe(this, user -> {
        //     if (user != null) {
        //         currentUser = user;
        //         populateForm();
        //     }
        // });
    }

    private void createSampleUser() {
        currentUser = new User("1", "John Doe", "john@example.com", User.UserType.OWNER);
        currentUser.setBio("I'm a property owner with 5 years of experience in rental management. I love providing comfortable and clean spaces for my tenants.");
        currentUser.setLocation("New York, NY");
        currentUser.setPhoneNumber("+1 (555) 123-4567");
        
        populateForm();
    }

    private void populateForm() {
        etName.setText(currentUser.getName());
        etEmail.setText(currentUser.getEmail());
        etPhone.setText(currentUser.getPhoneNumber());
        etLocation.setText(currentUser.getLocation());
        etBio.setText(currentUser.getBio());
        
        // Set spinner selection based on user type
        if (currentUser.getUserType() == User.UserType.OWNER) {
            spinnerUserType.setSelection(0);
        } else {
            spinnerUserType.setSelection(1);
        }
    }

    private void saveProfile() {
        // Validate form
        if (!validateForm()) {
            return;
        }

        // Update user object
        currentUser.setName(etName.getText().toString().trim());
        currentUser.setEmail(etEmail.getText().toString().trim());
        currentUser.setPhoneNumber(etPhone.getText().toString().trim());
        currentUser.setLocation(etLocation.getText().toString().trim());
        currentUser.setBio(etBio.getText().toString().trim());
        
        // Update user type based on spinner selection
        String selectedType = spinnerUserType.getSelectedItem().toString();
        if (selectedType.equals("Owner")) {
            currentUser.setUserType(User.UserType.OWNER);
        } else {
            currentUser.setUserType(User.UserType.RENTER);
        }

        // Save to database
        repository.updateUser(currentUser);

        // Return to profile activity with updated data
        Intent resultIntent = new Intent();
        resultIntent.putExtra("user_updated", true);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private boolean validateForm() {
        boolean isValid = true;

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name is required");
            isValid = false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            isValid = false;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Phone number is required");
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
} 