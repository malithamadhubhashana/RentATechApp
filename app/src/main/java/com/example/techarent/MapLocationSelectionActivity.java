package com.example.techarent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapLocationSelectionActivity extends AppCompatActivity implements OnMapReadyCallback {
    
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    
    // UI Components
    private ProgressBar progressBar;
    private TextView tvSelectedAddress, tvCoordinates;
    private Button btnCancel, btnConfirm;
    private FloatingActionButton fabMyLocation;
    
    // Location
    private LatLng selectedLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location_selection);
        
        // Setup action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Select Location");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        initializeViews();
        setupClickListeners();
        initializeLocation();
        initializeMap();
    }
    
    private void initializeViews() {
        progressBar = findViewById(R.id.progress_bar);
        tvSelectedAddress = findViewById(R.id.tv_selected_address);
        tvCoordinates = findViewById(R.id.tv_coordinates);
        btnCancel = findViewById(R.id.btn_cancel);
        btnConfirm = findViewById(R.id.btn_confirm);
        fabMyLocation = findViewById(R.id.fab_my_location);
    }
    
    private void setupClickListeners() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLocationSelection();
            }
        });
        
        fabMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });
    }
    
    private void initializeLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }
    
    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
    
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        
        // Configure map
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        
        // Set up camera move listener to update selected location
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                selectedLocation = googleMap.getCameraPosition().target;
                updateLocationInfo(selectedLocation);
            }
        });
        
        // Enable location if permission granted
        enableMyLocation();
        
        // Check if initial location was passed
        Intent intent = getIntent();
        if (intent.hasExtra("current_latitude") && intent.hasExtra("current_longitude")) {
            double lat = intent.getDoubleExtra("current_latitude", 0.0);
            double lng = intent.getDoubleExtra("current_longitude", 0.0);
            LatLng initialLocation = new LatLng(lat, lng);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 15f));
        } else {
            // Get current location
            getCurrentLocation();
        }
        
        // Hide loading
        progressBar.setVisibility(View.GONE);
    }
    
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    
    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
                        } else {
                            Toast.makeText(MapLocationSelectionActivity.this, 
                                "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    
    private void updateLocationInfo(LatLng location) {
        tvCoordinates.setText(String.format("%.6f, %.6f", location.latitude, location.longitude));
        
        // Get address for the location
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Geocoder geocoder = new Geocoder(MapLocationSelectionActivity.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                    
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (addresses != null && !addresses.isEmpty()) {
                                Address address = addresses.get(0);
                                String addressText = address.getAddressLine(0);
                                if (addressText != null && !addressText.isEmpty()) {
                                    tvSelectedAddress.setText(addressText);
                                } else {
                                    tvSelectedAddress.setText("Location selected");
                                }
                            } else {
                                tvSelectedAddress.setText("Location selected");
                            }
                        }
                    });
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvSelectedAddress.setText("Location selected");
                        }
                    });
                }
            }
        }).start();
    }
    
    private void confirmLocationSelection() {
        if (selectedLocation != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_latitude", selectedLocation.latitude);
            resultIntent.putExtra("selected_longitude", selectedLocation.longitude);
            resultIntent.putExtra("selected_address", tvSelectedAddress.getText().toString());
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission required", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
