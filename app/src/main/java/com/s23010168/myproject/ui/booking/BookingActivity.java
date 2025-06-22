package com.s23010168.myproject.ui.booking;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.s23010168.myproject.R;
import com.s23010168.myproject.data.repository.RentalRepository;
import com.s23010168.myproject.models.Listing;

public class BookingActivity extends AppCompatActivity {

    private TextView tvPropertyTitle, tvPropertyLocation, tvPropertyPrice;
    private TextView tvTotalCost, tvBookingSummary;
    private MaterialButton btnConfirmBooking, btnSelectDate, btnSelectTime;
    private MaterialButton btnBack;
    
    private RentalRepository repository;
    private Listing selectedListing;
    private double totalCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        repository = new RentalRepository(this);
        
        initViews();
        setupClickListeners();
        loadListingDetails();
    }

    private void initViews() {
        tvPropertyTitle = findViewById(R.id.tv_property_title);
        tvPropertyLocation = findViewById(R.id.tv_property_location);
        tvPropertyPrice = findViewById(R.id.tv_property_price);
        tvTotalCost = findViewById(R.id.tv_total_cost);
        tvBookingSummary = findViewById(R.id.tv_booking_summary);
        btnConfirmBooking = findViewById(R.id.btn_confirm_booking);
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnSelectTime = findViewById(R.id.btn_select_time);
        btnBack = findViewById(R.id.btn_back);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        btnConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmBooking();
            }
        });
    }

    private void loadListingDetails() {
        String listingId = getIntent().getStringExtra("listing_id");
        
        if (listingId != null) {
            // For demo purposes, create a sample listing
            createSampleListing();
            
            // In a real app, you would load from database
            // repository.getListingById(listingId).observe(this, listing -> {
            //     if (listing != null) {
            //         selectedListing = listing;
            //         displayListingDetails();
            //     }
            // });
        }
    }

    private void createSampleListing() {
        selectedListing = new Listing("1", "Cozy Downtown Apartment", 
            "Beautiful 2-bedroom apartment in the heart of downtown", 
            1200.0, "Downtown", "Apartment");
        
        displayListingDetails();
    }

    private void displayListingDetails() {
        tvPropertyTitle.setText(selectedListing.getTitle());
        tvPropertyLocation.setText(selectedListing.getLocation());
        tvPropertyPrice.setText("$" + String.format("%.0f", selectedListing.getPrice()) + "/month");
        
        // Calculate total cost (for demo, just use the monthly price)
        totalCost = selectedListing.getPrice();
        tvTotalCost.setText("$" + String.format("%.0f", totalCost));
        
        // Set booking summary
        tvBookingSummary.setText("Booking for " + selectedListing.getTitle() + 
            " located in " + selectedListing.getLocation());
    }

    private void showDatePicker() {
        // TODO: Implement date picker dialog
        // For now, just update the button text
        btnSelectDate.setText("Selected: Tomorrow");
    }

    private void showTimePicker() {
        // TODO: Implement time picker dialog
        // For now, just update the button text
        btnSelectTime.setText("Selected: 2:00 PM");
    }

    private void confirmBooking() {
        // Show loading state
        btnConfirmBooking.setEnabled(false);
        btnConfirmBooking.setText("Processing...");
        
        // TODO: Implement actual booking logic
        // For demo purposes, simulate booking process
        
        // Simulate API call delay
        btnConfirmBooking.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Show success message
                btnConfirmBooking.setText("Booking Confirmed!");
                btnConfirmBooking.setEnabled(false);
                
                // TODO: Navigate to booking confirmation screen
                // or show success dialog
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
} 