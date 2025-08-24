package com.example.techarent;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ItemDetailActivity extends AppCompatActivity {

    private TextView tvItemName, tvBrand, tvCategory, tvDescription, tvPrice, tvLocation;
    private Button btnRentNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Item Details");
        }

        initViews();
        loadItemData();
    }

    private void initViews() {
        tvItemName = findViewById(R.id.tv_item_name);
        tvBrand = findViewById(R.id.tv_brand);
        tvCategory = findViewById(R.id.tv_category);
        tvDescription = findViewById(R.id.tv_description);
        tvPrice = findViewById(R.id.tv_price);
        tvLocation = findViewById(R.id.tv_location);
        btnRentNow = findViewById(R.id.btn_rent_now);

        btnRentNow.setOnClickListener(v -> {
            // TODO: Implement booking functionality
            Toast.makeText(this, "Booking feature coming soon!", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadItemData() {
        // Get data from intent
        String itemName = getIntent().getStringExtra("item_name");
        String brand = getIntent().getStringExtra("brand");
        String category = getIntent().getStringExtra("category");
        String description = getIntent().getStringExtra("description");
        double price = getIntent().getDoubleExtra("price", 0.0);
        String location = getIntent().getStringExtra("location");

        // Set data to views
        tvItemName.setText(itemName != null ? itemName : "Unknown Item");
        tvBrand.setText("Brand: " + (brand != null ? brand : "Unknown"));
        tvCategory.setText("Category: " + (category != null ? category : "Unknown"));
        tvDescription.setText(description != null ? description : "No description available");
        tvPrice.setText(String.format("$%.2f per day", price));
        tvLocation.setText("Location: " + (location != null ? location : "Unknown"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
