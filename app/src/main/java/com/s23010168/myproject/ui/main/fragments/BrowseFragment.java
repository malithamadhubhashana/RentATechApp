package com.s23010168.myproject.ui.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.s23010168.myproject.R;
import com.s23010168.myproject.models.Listing;
import com.s23010168.myproject.ui.listing.AddListingActivity;
import com.s23010168.myproject.ui.listing.BrowseListingsActivity;
import com.s23010168.myproject.ui.listing.ListingsAdapter;

import java.util.ArrayList;
import java.util.List;

public class BrowseFragment extends Fragment {

    private RecyclerView rvListings;
    private ListingsAdapter listingsAdapter;
    private List<Listing> listings;
    private FloatingActionButton fabAddListing;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        
        initViews(view);
        setupRecyclerView();
        loadSampleData();
        setupClickListeners();
        
        return view;
    }

    private void initViews(View view) {
        rvListings = view.findViewById(R.id.rv_listings);
        fabAddListing = view.findViewById(R.id.fab_add_listing);
    }

    private void setupRecyclerView() {
        listings = new ArrayList<>();
        listingsAdapter = new ListingsAdapter(listings, new ListingsAdapter.OnListingClickListener() {
            @Override
            public void onListingClick(Listing listing) {
                // Navigate to listing details
                Intent intent = new Intent(getActivity(), com.s23010168.myproject.ui.listing.ListingDetailsActivity.class);
                intent.putExtra("listing_id", listing.getId());
                startActivity(intent);
            }
        });
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvListings.setLayoutManager(layoutManager);
        rvListings.setAdapter(listingsAdapter);
    }

    private void loadSampleData() {
        // Add sample listings
        listings.clear();
        listings.add(new Listing("1", "Cozy Downtown Apartment", 
            "Beautiful 2-bedroom apartment in the heart of downtown", 
            1200.0, "Downtown", "Apartment"));
        listings.add(new Listing("2", "Modern Studio Loft", 
            "Contemporary studio with high ceilings and city views", 
            900.0, "Midtown", "Studio"));
        listings.add(new Listing("3", "Family House with Garden", 
            "Spacious 3-bedroom house with private garden", 
            1800.0, "Suburbs", "House"));
        listings.add(new Listing("4", "Luxury Penthouse", 
            "Exclusive penthouse with panoramic city views", 
            2500.0, "Uptown", "Penthouse"));
        
        listingsAdapter.notifyDataSetChanged();
    }

    private void setupClickListeners() {
        fabAddListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddListingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when returning to this fragment
        loadSampleData();
    }
} 