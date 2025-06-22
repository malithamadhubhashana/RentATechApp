package com.s23010168.myproject.ui.review;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.s23010168.myproject.R;

public class ReviewActivity extends AppCompatActivity {

    private TextView tvPropertyTitle;
    private RatingBar ratingBar;
    private EditText etReview;
    private MaterialButton btnSubmitReview, btnBack;
    private ImageView ivBack;
    
    private String listingId;
    private float currentRating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        listingId = getIntent().getStringExtra("listing_id");
        
        initViews();
        setupClickListeners();
        loadPropertyDetails();
    }

    private void initViews() {
        tvPropertyTitle = findViewById(R.id.tv_property_title);
        ratingBar = findViewById(R.id.rating_bar);
        etReview = findViewById(R.id.et_review);
        btnSubmitReview = findViewById(R.id.btn_submit_review);
        btnBack = findViewById(R.id.btn_back);
        ivBack = findViewById(R.id.iv_back);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                currentRating = rating;
            }
        });

        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });
    }

    private void loadPropertyDetails() {
        // For demo purposes, set a sample property title
        tvPropertyTitle.setText("Cozy Downtown Apartment");
        
        // In a real app, you would load from database
        // repository.getListingById(listingId).observe(this, listing -> {
        //     if (listing != null) {
        //         tvPropertyTitle.setText(listing.getTitle());
        //     }
        // });
    }

    private void submitReview() {
        String reviewText = etReview.getText().toString().trim();
        
        // Validate input
        if (currentRating == 0) {
            Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (TextUtils.isEmpty(reviewText)) {
            Toast.makeText(this, "Please write a review", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (reviewText.length() < 10) {
            Toast.makeText(this, "Review must be at least 10 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Show loading state
        btnSubmitReview.setEnabled(false);
        btnSubmitReview.setText("Submitting...");
        
        // TODO: Save review to database
        // Review review = new Review(listingId, currentRating, reviewText, System.currentTimeMillis());
        // repository.saveReview(review);
        
        // Simulate API call delay
        btnSubmitReview.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Show success message
                Toast.makeText(ReviewActivity.this, "Review submitted successfully!", Toast.LENGTH_SHORT).show();
                
                // Navigate back
                finish();
            }
        }, 1500);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
} 