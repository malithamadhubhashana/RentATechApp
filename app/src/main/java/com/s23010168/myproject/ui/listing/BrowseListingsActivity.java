package com.s23010168.myproject.ui.listing;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.s23010168.myproject.R;
import com.s23010168.myproject.data.repository.RentalRepository;
import com.s23010168.myproject.models.Listing;
import com.s23010168.myproject.ui.map.MapActivity;

import java.util.ArrayList;
import java.util.List;

public class BrowseListingsActivity extends AppCompatActivity {

    private EditText etSearch;
    private ImageView ivFilter;
    private RecyclerView rvListings;
    private MaterialButton btnMapView;
    private TextView tvResultsCount;
    private ChipGroup chipGroupFilters;
    
    private RentalRepository repository;
    private ListingsAdapter listingsAdapter;
    private List<Listing> allListings;
    private List<Listing> filteredListings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_listings);

        repository = new RentalRepository(this);
        allListings = new ArrayList<>();
        filteredListings = new ArrayList<>();

        initViews();
        setupRecyclerView();
        setupSearch();
        setupClickListeners();
        loadListings();
    }

    private void initViews() {
        etSearch = findViewById(R.id.et_search);
        ivFilter = findViewById(R.id.iv_filter);
        rvListings = findViewById(R.id.rv_listings);
        btnMapView = findViewById(R.id.btn_map_view);
        tvResultsCount = findViewById(R.id.tv_results_count);
        chipGroupFilters = findViewById(R.id.chip_group_filters);
    }

    private void setupRecyclerView() {
        listingsAdapter = new ListingsAdapter(filteredListings, new ListingsAdapter.OnListingClickListener() {
            @Override
            public void onListingClick(Listing listing) {
                Intent intent = new Intent(BrowseListingsActivity.this, ListingDetailsActivity.class);
                intent.putExtra("listing_id", listing.getId());
                startActivity(intent);
            }
        });
        
        rvListings.setLayoutManager(new LinearLayoutManager(this));
        rvListings.setAdapter(listingsAdapter);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterListings(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupClickListeners() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

        btnMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrowseListingsActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadListings() {
        // For demo purposes, create some sample listings
        createSampleListings();
        
        // In a real app, you would load from database
        // repository.getAvailableListings().observe(this, listings -> {
        //     allListings.clear();
        //     allListings.addAll(listings);
        //     filterListings("");
        // });
    }

    private void createSampleListings() {
        allListings.clear();
        
        // Sample listings
        allListings.add(new Listing("1", "Cozy Downtown Apartment", 
            "Beautiful 2-bedroom apartment in the heart of downtown", 1200.0, "Downtown", "Apartment"));
        allListings.add(new Listing("2", "Luxury Beach House", 
            "Stunning beachfront property with ocean views", 2500.0, "Beach Area", "House"));
        allListings.add(new Listing("3", "Modern Studio Loft", 
            "Contemporary studio with high ceilings and city views", 800.0, "City Center", "Studio"));
        allListings.add(new Listing("4", "Family Villa", 
            "Spacious 4-bedroom villa perfect for families", 1800.0, "Suburbs", "Villa"));
        allListings.add(new Listing("5", "Mountain Cabin", 
            "Rustic cabin with mountain views and hiking trails", 950.0, "Mountain Area", "Cabin"));
        
        filterListings("");
    }

    private void filterListings(String query) {
        filteredListings.clear();
        
        for (Listing listing : allListings) {
            if (query.isEmpty() || 
                listing.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                listing.getDescription().toLowerCase().contains(query.toLowerCase()) ||
                listing.getLocation().toLowerCase().contains(query.toLowerCase())) {
                filteredListings.add(listing);
            }
        }
        
        listingsAdapter.notifyDataSetChanged();
        updateResultsCount();
    }

    private void updateResultsCount() {
        tvResultsCount.setText(filteredListings.size() + " properties found");
    }

    private void showFilterDialog() {
        // TODO: Implement filter dialog
        // Show dialog with price range, category, location filters
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
} 