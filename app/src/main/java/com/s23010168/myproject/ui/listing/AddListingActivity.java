package com.s23010168.myproject.ui.listing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.s23010168.myproject.R;
import com.s23010168.myproject.data.repository.RentalRepository;
import com.s23010168.myproject.models.Listing;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddListingActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextInputLayout tilTitle, tilDescription, tilPrice, tilLocation;
    private EditText etTitle, etDescription, etPrice, etLocation;
    private Spinner spinnerCategory;
    private CheckBox cbAvailable;
    private MaterialButton btnUploadImages, btnSave;
    private ImageView ivBack;
    
    private RentalRepository repository;
    private List<Uri> selectedImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);

        repository = new RentalRepository(this);
        selectedImages = new ArrayList<>();

        initViews();
        setupSpinner();
        setupClickListeners();
    }

    private void initViews() {
        tilTitle = findViewById(R.id.til_title);
        tilDescription = findViewById(R.id.til_description);
        tilPrice = findViewById(R.id.til_price);
        tilLocation = findViewById(R.id.til_location);
        
        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etPrice = findViewById(R.id.et_price);
        etLocation = findViewById(R.id.et_location);
        
        spinnerCategory = findViewById(R.id.spinner_category);
        cbAvailable = findViewById(R.id.cb_available);
        btnUploadImages = findViewById(R.id.btn_upload_images);
        btnSave = findViewById(R.id.btn_save);
        ivBack = findViewById(R.id.iv_back);
    }

    private void setupSpinner() {
        String[] categories = {"Apartment", "House", "Villa", "Condo", "Studio", "Room", "Cabin", "Beach House", "Mountain Lodge", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnUploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveListing();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), PICK_IMAGE_REQUEST);
    }

    private void saveListing() {
        // Reset errors
        tilTitle.setError(null);
        tilDescription.setError(null);
        tilPrice.setError(null);
        tilLocation.setError(null);

        // Get values from input fields
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        boolean isAvailable = cbAvailable.isChecked();

        boolean cancel = false;
        View focusView = null;

        // Validate title
        if (TextUtils.isEmpty(title)) {
            tilTitle.setError("Title is required");
            focusView = etTitle;
            cancel = true;
        }

        // Validate description
        if (TextUtils.isEmpty(description)) {
            tilDescription.setError("Description is required");
            focusView = etDescription;
            cancel = true;
        }

        // Validate price
        if (TextUtils.isEmpty(priceStr)) {
            tilPrice.setError("Price is required");
            focusView = etPrice;
            cancel = true;
        } else {
            try {
                double price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    tilPrice.setError("Price must be greater than 0");
                    focusView = etPrice;
                    cancel = true;
                }
            } catch (NumberFormatException e) {
                tilPrice.setError("Invalid price format");
                focusView = etPrice;
                cancel = true;
            }
        }

        // Validate location
        if (TextUtils.isEmpty(location)) {
            tilLocation.setError("Location is required");
            focusView = etLocation;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Create and save listing
            createListing(title, description, Double.parseDouble(priceStr), location, category, isAvailable);
        }
    }

    private void createListing(String title, String description, double price, String location, String category, boolean isAvailable) {
        // Show loading state
        btnSave.setEnabled(false);
        btnSave.setText("Saving...");

        // Create new listing
        Listing listing = new Listing(UUID.randomUUID().toString(), title, description, price, location, category);
        listing.setAvailable(isAvailable);
        
        // TODO: Add current user as owner
        listing.setOwnerId("current_user_id");
        listing.setOwnerName("Current User");

        // Save to database
        repository.insertListing(listing);

        // Show success message
        Toast.makeText(this, "Listing created successfully!", Toast.LENGTH_SHORT).show();

        // Navigate back
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                // Multiple images selected
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedImages.add(imageUri);
                }
            } else if (data.getData() != null) {
                // Single image selected
                Uri imageUri = data.getData();
                selectedImages.add(imageUri);
            }
            
            // Update button text
            btnUploadImages.setText(selectedImages.size() + " images selected");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
} 