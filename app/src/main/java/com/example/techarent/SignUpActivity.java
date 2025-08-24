package com.example.techarent;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {
    
    private TextInputEditText etFullName, etEmail, etPhone, etPassword, etConfirmPassword;
    private Button btnCreateAccount;
    private TextView tvSignInLink;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        
        // Initialize database helper
        dbHelper = new DatabaseHelper(this);
        
        // Setup action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sign Up");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        initializeViews();
        setupClickListeners();
    }
    
    private void initializeViews() {
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnCreateAccount = findViewById(R.id.btn_create_account);
        tvSignInLink = findViewById(R.id.tv_sign_in_link);
        progressBar = findViewById(R.id.progress_bar);
    }
    
    private void setupClickListeners() {
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });
        
        tvSignInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    
    private void attemptSignUp() {
        // Reset errors
        etFullName.setError(null);
        etEmail.setError(null);
        etPhone.setError(null);
        etPassword.setError(null);
        etConfirmPassword.setError(null);
        
        // Get input values
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        
        boolean hasError = false;
        
        // Validate inputs
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full name is required");
            hasError = true;
        }
        
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            hasError = true;
        } else if (!isValidEmail(email)) {
            etEmail.setError(getString(R.string.invalid_email));
            hasError = true;
        }
        
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone number is required");
            hasError = true;
        }
        
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            hasError = true;
        } else if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            hasError = true;
        }
        
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Confirm password is required");
            hasError = true;
        } else if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError(getString(R.string.passwords_dont_match));
            hasError = true;
        }
        
        if (hasError) {
            return;
        }
        
        // Show progress
        showProgress(true);
        
        // Simulate network delay (replace with actual authentication in real app)
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                performSignUp(fullName, email, phone, password);
            }
        }, 1500);
    }
    
    private void performSignUp(String fullName, String email, String phone, String password) {
        // Check if email already exists
        if (dbHelper.emailExists(email)) {
            showProgress(false);
            Toast.makeText(this, getString(R.string.email_exists), Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Create new user
        long userId = dbHelper.createUser(fullName, email, phone, password);
        
        showProgress(false);
        
        if (userId != -1) {
            // Sign up successful - automatically log in the user
            dbHelper.setUserLoggedIn(email);
            Toast.makeText(this, "Account created successfully! Please login to continue.", Toast.LENGTH_SHORT).show();
            
            // Navigate to welcome activity so user can login
            Intent intent = new Intent(SignUpActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Sign up failed
            Toast.makeText(this, "Failed to create account. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            btnCreateAccount.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            btnCreateAccount.setVisibility(View.VISIBLE);
        }
    }
    
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
