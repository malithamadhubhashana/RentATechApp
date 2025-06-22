package com.s23010168.myproject.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.s23010168.myproject.R;
import com.s23010168.myproject.data.repository.RentalRepository;
import com.s23010168.myproject.models.User;
import com.s23010168.myproject.ui.main.MainActivity;

import java.util.UUID;

public class SignupActivity extends AppCompatActivity {

    private TextInputLayout tilName, tilEmail, tilPassword, tilConfirmPassword;
    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword;
    private RadioGroup rgUserType;
    private RadioButton rbRenter, rbOwner;
    private MaterialButton btnSignUp;
    private RentalRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize repository
        repository = new RentalRepository(this);

        // Initialize views
        initViews();
        setupClickListeners();
    }

    private void initViews() {
        tilName = findViewById(R.id.til_name);
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        tilConfirmPassword = findViewById(R.id.til_confirm_password);
        
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        
        rgUserType = findViewById(R.id.rg_user_type);
        rbRenter = findViewById(R.id.rb_renter);
        rbOwner = findViewById(R.id.rb_owner);
        
        btnSignUp = findViewById(R.id.btn_sign_up);
    }

    private void setupClickListeners() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.tv_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignup();
            }
        });
    }

    private void attemptSignup() {
        // Reset errors
        tilName.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);

        // Get values from input fields
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for valid name
        if (TextUtils.isEmpty(name)) {
            tilName.setError("Name is required");
            focusView = etName;
            cancel = true;
        }

        // Check for valid email
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email is required");
            focusView = etEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            tilEmail.setError("Invalid email format");
            focusView = etEmail;
            cancel = true;
        }

        // Check for valid password
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Password is required");
            focusView = etPassword;
            cancel = true;
        } else if (password.length() < 6) {
            tilPassword.setError("Password must be at least 6 characters");
            focusView = etPassword;
            cancel = true;
        }

        // Check for password confirmation
        if (TextUtils.isEmpty(confirmPassword)) {
            tilConfirmPassword.setError("Please confirm your password");
            focusView = etConfirmPassword;
            cancel = true;
        } else if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError("Passwords do not match");
            focusView = etConfirmPassword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt signup and focus the first form field with an error
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and perform the actual signup
            performSignup(name, email, password);
        }
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void performSignup(String name, String email, String password) {
        // Show loading state
        btnSignUp.setEnabled(false);
        btnSignUp.setText("Creating Account...");

        // Check if user already exists
        if (repository.getUserByEmail(email) != null) {
            Toast.makeText(this, "User with this email already exists", Toast.LENGTH_SHORT).show();
            btnSignUp.setEnabled(true);
            btnSignUp.setText("Sign Up");
            return;
        }

        // Create new user
        User.UserType userType = rbRenter.isChecked() ? User.UserType.RENTER : User.UserType.OWNER;
        User newUser = new User(UUID.randomUUID().toString(), name, email, userType);

        // Save user to database
        repository.insertUser(newUser);

        // Show success message
        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();

        // Navigate to main activity
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
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