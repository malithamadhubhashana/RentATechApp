package com.example.techarent;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {
    
    private TextInputEditText etItemName, etBrand, etDescription, etPrice, etLocation;
    private Spinner spinnerCategory;
    private Button btnCurrentLocation, btnSelectMap, btnSaveItem, btnTakePhoto, btnSelectPhoto;
    private TextView tvLocationStatus, tvLocationIndicator, tvPhotoStatus;
    private ImageView ivDevicePhoto;
    private ProgressBar progressBar;
    
    private DatabaseHelper dbHelper;
    private FusedLocationProviderClient fusedLocationClient;
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;
    
    // Photo handling
    private String currentPhotoPath;
    private Uri photoUri;
    
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int MAP_SELECTION_REQUEST_CODE = 1002;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1003;
    private static final int CAMERA_REQUEST_CODE = 1004;
    private static final int GALLERY_REQUEST_CODE = 1005;
    
    private String[] categories = {"Laptops", "Smartphones", "Tablets", "Cameras", "Gaming", "Audio"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        
        // Initialize database helper
        dbHelper = new DatabaseHelper(this);
        
        // Setup action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Item");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        initializeViews();
        setupCategorySpinner();
        setupClickListeners();
        
        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        
        // Set initial location indicator state
        updateLocationIndicator();
        
        // Check if location was passed from MapActivity
        Intent intent = getIntent();
        if (intent.hasExtra("latitude") && intent.hasExtra("longitude")) {
            currentLatitude = intent.getDoubleExtra("latitude", 0.0);
            currentLongitude = intent.getDoubleExtra("longitude", 0.0);
            getAddressFromLocation(currentLatitude, currentLongitude);
        }
    }
    
    private void initializeViews() {
        etItemName = findViewById(R.id.et_item_name);
        etBrand = findViewById(R.id.et_brand);
        etDescription = findViewById(R.id.et_description);
        etPrice = findViewById(R.id.et_price);
        etLocation = findViewById(R.id.et_location);
        spinnerCategory = findViewById(R.id.spinner_category);
        btnCurrentLocation = findViewById(R.id.btn_current_location);
        btnSelectMap = findViewById(R.id.btn_select_map);
        btnSaveItem = findViewById(R.id.btn_save_item);
        btnTakePhoto = findViewById(R.id.btn_take_photo);
        btnSelectPhoto = findViewById(R.id.btn_select_photo);
        tvLocationStatus = findViewById(R.id.tv_location_status);
        tvLocationIndicator = findViewById(R.id.tv_location_indicator);
        tvPhotoStatus = findViewById(R.id.tv_photo_status);
        ivDevicePhoto = findViewById(R.id.iv_device_photo);
        progressBar = findViewById(R.id.progress_bar);
    }
    
    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }
    
    private void setupClickListeners() {
        btnCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });
        
        btnSelectMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapForLocationSelection();
            }
        });
        
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        
        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhotoFromGallery();
            }
        });
        
        btnSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });
    }
    
    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED) {
            // Request location permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        
        showLocationStatus(getString(R.string.getting_location), true);
        
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLatitude = location.getLatitude();
                            currentLongitude = location.getLongitude();
                            getAddressFromLocation(currentLatitude, currentLongitude);
                        } else {
                            showLocationStatus("Unable to get location. Please try again.", false);
                        }
                    }
                });
    }
    
    private void openMapForLocationSelection() {
        Intent intent = new Intent(this, MapLocationSelectionActivity.class);
        if (currentLatitude != 0.0 && currentLongitude != 0.0) {
            intent.putExtra("current_latitude", currentLatitude);
            intent.putExtra("current_longitude", currentLongitude);
        }
        startActivityForResult(intent, MAP_SELECTION_REQUEST_CODE);
    }
    
    private void saveItem() {
        // Reset errors
        etItemName.setError(null);
        etBrand.setError(null);
        etDescription.setError(null);
        etPrice.setError(null);
        etLocation.setError(null);
        
        // Get input values
        String itemName = etItemName.getText().toString().trim();
        String brand = etBrand.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String priceText = etPrice.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String category = categories[spinnerCategory.getSelectedItemPosition()];
        
        boolean hasError = false;
        
        // Validate inputs
        if (TextUtils.isEmpty(itemName)) {
            etItemName.setError("Item name is required");
            hasError = true;
        }
        
        if (TextUtils.isEmpty(brand)) {
            etBrand.setError("Brand is required");
            hasError = true;
        }
        
        if (TextUtils.isEmpty(description)) {
            etDescription.setError("Description is required");
            hasError = true;
        }
        
        if (TextUtils.isEmpty(priceText)) {
            etPrice.setError("Price is required");
            hasError = true;
        }
        
        if (TextUtils.isEmpty(location)) {
            etLocation.setError("Location is required");
            hasError = true;
        }
        
        if (hasError) {
            return;
        }
        
        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price <= 0) {
                etPrice.setError("Price must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            etPrice.setError("Invalid price format");
            return;
        }
        
        // Get current user
        String userEmail = dbHelper.getLoggedInUserEmail();
        if (userEmail == null) {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show();
            return;
        }
        
        User currentUser = dbHelper.getUserByEmail(userEmail);
        if (currentUser == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Show progress
        showProgress(true);
        
        // Save item to database
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                long listingId = dbHelper.createListing(
                    currentUser.getId(),
                    itemName,
                    brand,
                    category,
                    description,
                    price,
                    location,
                    currentLatitude,
                    currentLongitude
                );
                
                showProgress(false);
                
                if (listingId != -1) {
                    Toast.makeText(AddItemActivity.this, 
                        getString(R.string.item_saved_success), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddItemActivity.this, 
                        "Failed to save item. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }, 1000);
    }
    
    private void takePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.CAMERA}, 
                CAMERA_PERMISSION_REQUEST_CODE);
            return;
        }
        
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error creating image file", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this,
                    getPackageName() + ".fileprovider",
                    photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
    
    private void selectPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }
    
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    
    private void updatePhotoStatus() {
        if (currentPhotoPath != null || photoUri != null) {
            tvPhotoStatus.setText("✅ Added");
            tvPhotoStatus.setTextColor(getResources().getColor(R.color.accent_green));
        } else {
            tvPhotoStatus.setText("Optional");
            tvPhotoStatus.setTextColor(getResources().getColor(R.color.text_secondary));
        }
    }
    
    private void displayPhoto(String imagePath) {
        if (imagePath != null && new File(imagePath).exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap != null) {
                ivDevicePhoto.setImageBitmap(bitmap);
                ivDevicePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ivDevicePhoto.setColorFilter(null); // Remove tint
                updatePhotoStatus();
            }
        }
    }
    
    private void displayPhoto(Uri imageUri) {
        if (imageUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ivDevicePhoto.setImageBitmap(bitmap);
                ivDevicePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ivDevicePhoto.setColorFilter(null); // Remove tint
                updatePhotoStatus();
            } catch (IOException e) {
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            btnSaveItem.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            btnSaveItem.setVisibility(View.VISIBLE);
        }
    }
    
    private void showLocationStatus(String message, boolean showProgress) {
        tvLocationStatus.setText(message);
        tvLocationStatus.setVisibility(View.VISIBLE);
        
        if (!showProgress) {
            // Hide status after 3 seconds
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvLocationStatus.setVisibility(View.GONE);
                }
            }, 3000);
        }
    }
    
    private void updateLocationIndicator() {
        String locationText = etLocation.getText().toString().trim();
        if (!locationText.isEmpty() && (currentLatitude != 0.0 || currentLongitude != 0.0)) {
            tvLocationIndicator.setText("✅ Set");
            tvLocationIndicator.setTextColor(getResources().getColor(R.color.accent_green));
        } else {
            tvLocationIndicator.setText("Required");
            tvLocationIndicator.setTextColor(getResources().getColor(R.color.accent_orange));
        }
    }
    
    private void getAddressFromLocation(double latitude, double longitude) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressText = address.getAddressLine(0);
                if (addressText != null) {
                    etLocation.setText(addressText);
                    showLocationStatus("Location updated: " + addressText, false);
                    updateLocationIndicator();
                } else {
                    showLocationStatus("Location coordinates: " + 
                        String.format("%.6f, %.6f", latitude, longitude), false);
                    updateLocationIndicator();
                }
            } else {
                showLocationStatus("Location coordinates: " + 
                    String.format("%.6f, %.6f", latitude, longitude), false);
            }
        } catch (IOException e) {
            showLocationStatus("Location coordinates: " + 
                String.format("%.6f, %.6f", latitude, longitude), false);
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                          @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                showLocationStatus(getString(R.string.location_permission_required), false);
            }
        } else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == MAP_SELECTION_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                currentLatitude = data.getDoubleExtra("selected_latitude", 0.0);
                currentLongitude = data.getDoubleExtra("selected_longitude", 0.0);
                String selectedAddress = data.getStringExtra("selected_address");
                
                if (selectedAddress != null && !selectedAddress.isEmpty()) {
                    etLocation.setText(selectedAddress);
                }
                
                showLocationStatus("Location selected from map", false);
                updateLocationIndicator();
            }
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Photo captured, display it
            displayPhoto(currentPhotoPath);
            Toast.makeText(this, "Photo captured successfully!", Toast.LENGTH_SHORT).show();
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                photoUri = data.getData();
                displayPhoto(photoUri);
                Toast.makeText(this, "Photo selected successfully!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
