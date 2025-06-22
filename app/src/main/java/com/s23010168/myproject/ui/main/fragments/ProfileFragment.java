package com.s23010168.myproject.ui.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.s23010168.myproject.R;
import com.s23010168.myproject.ui.auth.LoginActivity;
import com.s23010168.myproject.ui.profile.ProfileActivity;

public class ProfileFragment extends Fragment {

    private TextView tvUserName, tvUserEmail, tvUserRole;
    private MaterialButton btnEditProfile, btnSettings, btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        initViews(view);
        setupClickListeners();
        loadUserData();
        
        return view;
    }

    private void initViews(View view) {
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvUserEmail = view.findViewById(R.id.tv_user_email);
        tvUserRole = view.findViewById(R.id.tv_user_role);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnSettings = view.findViewById(R.id.btn_settings);
        btnLogout = view.findViewById(R.id.btn_logout);
    }

    private void setupClickListeners() {
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Navigate to settings
                // Intent intent = new Intent(getActivity(), SettingsActivity.class);
                // startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear user session and navigate to login
                logout();
            }
        });
    }

    private void loadUserData() {
        // For demo purposes, set sample user data
        tvUserName.setText("John Doe");
        tvUserEmail.setText("john.doe@example.com");
        tvUserRole.setText("Tenant");
        
        // In a real app, you would load from SharedPreferences or database
        // SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        // String userName = prefs.getString("user_name", "User");
        // String userEmail = prefs.getString("user_email", "user@example.com");
        // String userRole = prefs.getString("user_role", "Tenant");
        // 
        // tvUserName.setText(userName);
        // tvUserEmail.setText(userEmail);
        // tvUserRole.setText(userRole);
    }

    private void logout() {
        // Clear user session
        // SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        // SharedPreferences.Editor editor = prefs.edit();
        // editor.clear();
        // editor.apply();
        
        // Navigate to login screen
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
} 