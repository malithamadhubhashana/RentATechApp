package com.s23010168.myproject.ui.listing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.s23010168.myproject.R;
import com.s23010168.myproject.data.repository.RentalRepository;
import com.s23010168.myproject.models.Listing;
import com.s23010168.myproject.ui.booking.BookingActivity;
import com.s23010168.myproject.ui.messages.ChatActivity;
import com.s23010168.myproject.ui.review.ReviewActivity;

import java.util.ArrayList;
import java.util.List;

public class ListingDetailsActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ImagePagerAdapter imageAdapter;
    private TextView tvTitle, tvDescription, tvPrice, tvLocation, tvPropertyType;
    private TextView tvOwnerName, tvOwnerRating;
    private MaterialButton btnBookNow, btnContactOwner, btnBack;
    private ImageView ivBack;
    
    private RentalRepository repository;
    private Listing currentListing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_details);

        repository = new RentalRepository(this);
        
        initViews();
        setupImageCarousel();
        setupClickListeners();
        loadListingDetails();
    }

    private void initViews() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        tvTitle = findViewById(R.id.tv_title);
        tvDescription = findViewById(R.id.tv_description);
        tvPrice = findViewById(R.id.tv_price);
        tvLocation = findViewById(R.id.tv_location);
        tvPropertyType = findViewById(R.id.tv_property_type);
        tvOwnerName = findViewById(R.id.tv_owner_name);
        tvOwnerRating = findViewById(R.id.tv_owner_rating);
        btnBookNow = findViewById(R.id.btn_book_now);
        btnContactOwner = findViewById(R.id.btn_contact_owner);
        btnBack = findViewById(R.id.btn_back);
        ivBack = findViewById(R.id.iv_back);
    }

    private void setupImageCarousel() {
        // Create sample images for the carousel
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://example.com/image1.jpg");
        imageUrls.add("https://example.com/image2.jpg");
        imageUrls.add("https://example.com/image3.jpg");
        
        imageAdapter = new ImagePagerAdapter(imageUrls);
        viewPager.setAdapter(imageAdapter);
        
        // Connect ViewPager2 with TabLayout
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Tab configuration
        }).attach();
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

        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to booking activity
                Intent intent = new Intent(ListingDetailsActivity.this, BookingActivity.class);
                intent.putExtra("listing_id", currentListing.getId());
                startActivity(intent);
            }
        });

        btnContactOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to chat activity
                Intent intent = new Intent(ListingDetailsActivity.this, ChatActivity.class);
                intent.putExtra("owner_id", currentListing.getOwnerId());
                intent.putExtra("owner_name", currentListing.getOwnerName());
                startActivity(intent);
            }
        });

        // Add review button click listener
        findViewById(R.id.btn_write_review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListingDetailsActivity.this, ReviewActivity.class);
                intent.putExtra("listing_id", currentListing.getId());
                startActivity(intent);
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
            //         currentListing = listing;
            //         displayListingDetails();
            //     }
            // });
        }
    }

    private void createSampleListing() {
        currentListing = new Listing("1", "Cozy Downtown Apartment", 
            "Beautiful 2-bedroom apartment in the heart of downtown. This modern apartment features:\n\n" +
            "• 2 spacious bedrooms with large windows\n" +
            "• Fully equipped kitchen with stainless steel appliances\n" +
            "• In-unit washer and dryer\n" +
            "• Balcony with city views\n" +
            "• Gym and pool access\n" +
            "• Underground parking included\n" +
            "• 24/7 security and concierge service\n\n" +
            "Perfect for professionals or small families. Located near shopping, restaurants, and public transportation.",
            1200.0, "Downtown", "Apartment");
        currentListing.setOwnerId("owner_1");
        currentListing.setOwnerName("Sarah Johnson");
        
        displayListingDetails();
    }

    private void displayListingDetails() {
        tvTitle.setText(currentListing.getTitle());
        tvDescription.setText(currentListing.getDescription());
        tvPrice.setText("$" + String.format("%.0f", currentListing.getPrice()) + "/month");
        tvLocation.setText(currentListing.getLocation());
        tvPropertyType.setText(currentListing.getCategory());
        tvOwnerName.setText("Owner: " + currentListing.getOwnerName());
        tvOwnerRating.setText("★ 4.8 (24 reviews)");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
} 