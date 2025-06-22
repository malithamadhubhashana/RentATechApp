package com.s23010168.myproject.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.s23010168.myproject.R;
import com.s23010168.myproject.ui.listing.AddListingActivity;
import com.s23010168.myproject.ui.main.fragments.BrowseFragment;
import com.s23010168.myproject.ui.main.fragments.HomeFragment;
import com.s23010168.myproject.ui.main.fragments.MessagesFragment;
import com.s23010168.myproject.ui.main.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabAdd;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupBottomNavigation();
        setupClickListeners();
        
        // Load default fragment (Home)
        loadFragment(new HomeFragment());
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fabAdd = findViewById(R.id.fab_add);
        tvWelcome = findViewById(R.id.tv_welcome);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    
                    String title = item.getTitle().toString();
                    switch (title) {
                        case "Home":
                            fragment = new HomeFragment();
                            break;
                        case "Browse":
                            fragment = new BrowseFragment();
                            break;
                        case "Messages":
                            fragment = new MessagesFragment();
                            break;
                        case "Profile":
                            fragment = new ProfileFragment();
                            break;
                    }
                    
                    if (fragment != null) {
                        loadFragment(fragment);
                        return true;
                    }
                    return false;
                }
            });
    }

    private void setupClickListeners() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddListingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
} 