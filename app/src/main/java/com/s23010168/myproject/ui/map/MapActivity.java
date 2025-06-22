package com.s23010168.myproject.ui.map;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.s23010168.myproject.R;

public class MapActivity extends AppCompatActivity {

    private ImageView ivBack;
    private MaterialButton btnListingsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initViews();
        setupClickListeners();
        setupMap();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        btnListingsView = findViewById(R.id.btn_listings_view);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnListingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to listings view
                finish();
            }
        });
    }

    private void setupMap() {
        // TODO: Implement Google Maps integration
        // For now, this is a placeholder for the map functionality
        
        // In a real implementation, you would:
        // 1. Initialize Google Maps
        // 2. Add markers for rental properties
        // 3. Handle marker clicks to show property details
        // 4. Implement clustering for multiple properties
        // 5. Add search and filter functionality
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
} 