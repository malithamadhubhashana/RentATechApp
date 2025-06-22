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

import com.google.android.material.card.MaterialCardView;
import com.s23010168.myproject.R;
import com.s23010168.myproject.ui.listing.BrowseListingsActivity;
import com.s23010168.myproject.ui.map.MapActivity;

public class HomeFragment extends Fragment {

    private TextView tvWelcomeMessage;
    private MaterialCardView cardBrowseListings, cardMapView, cardMyListings, cardFavorites;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        initViews(view);
        setupClickListeners();
        loadUserData();
        
        return view;
    }

    private void initViews(View view) {
        tvWelcomeMessage = view.findViewById(R.id.tv_welcome_message);
        cardBrowseListings = view.findViewById(R.id.card_browse_listings);
        cardMapView = view.findViewById(R.id.card_map_view);
        cardMyListings = view.findViewById(R.id.card_my_listings);
        cardFavorites = view.findViewById(R.id.card_favorites);
    }

    private void setupClickListeners() {
        cardBrowseListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BrowseListingsActivity.class);
                startActivity(intent);
            }
        });

        cardMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
            }
        });

        cardMyListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Navigate to user's listings
                // Intent intent = new Intent(getActivity(), MyListingsActivity.class);
                // startActivity(intent);
            }
        });

        cardFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Navigate to favorites
                // Intent intent = new Intent(getActivity(), FavoritesActivity.class);
                // startActivity(intent);
            }
        });
    }

    private void loadUserData() {
        // For demo purposes, set a welcome message
        tvWelcomeMessage.setText("Welcome back, User!");
        
        // In a real app, you would load user data from SharedPreferences or database
        // String userName = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        //     .getString("user_name", "User");
        // tvWelcomeMessage.setText("Welcome back, " + userName + "!");
    }
} 