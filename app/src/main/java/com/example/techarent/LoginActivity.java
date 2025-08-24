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

public class LoginActivity extends AppCompatActivity {
    
    private TextInputEditText etEmail, etPassword;
    private Button btnSignIn;
    private TextView tvForgotPassword, tvSignUpLink;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Initialize database helper
        dbHelper = new DatabaseHelper(this);
        
        // Setup action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sign In");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        initializeViews();
        setupClickListeners();
    }
    
    private void initializeViews() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvSignUpLink = findViewById(R.id.tv_sign_up_link);
        progressBar = findViewById(R.id.progress_bar);
    }
    
    private void setupClickListeners() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement forgot password functionality
                Toast.makeText(LoginActivity.this, "Forgot password feature coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
        
        tvSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    
    private void attemptLogin() {
        // Reset errors
        etEmail.setError(null);
        etPassword.setError(null);
        
        // Get input values
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        boolean hasError = false;
        
        // Validate inputs
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            hasError = true;
        } else if (!isValidEmail(email)) {
            etEmail.setError(getString(R.string.invalid_email));
            hasError = true;
        }
        
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
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
                performLogin(email, password);
            }
        }, 1000);
    }
    
    private void performLogin(String email, String password) {
        boolean loginSuccess = dbHelper.validateUser(email, password);
        
        showProgress(false);
        
        if (loginSuccess) {
            // Login successful
            dbHelper.setUserLoggedIn(email);
            Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
            
            // Navigate to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Login failed
            Toast.makeText(this, getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.VISIBLE);
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
