package com.example.techarent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techarent.adapter.DeviceAdapter;
import com.example.techarent.data.DataManager;
import com.example.techarent.model.TechDevice;

import java.util.List;

public class BrowseActivity extends AppCompatActivity implements DeviceAdapter.OnDeviceClickListener {

    private RecyclerView recyclerViewItems;
    private Spinner spinnerCategory, spinnerPriceRange;
    private DeviceAdapter deviceAdapter;
    private DataManager dataManager;
    private List<TechDevice> currentDevices;
    private String selectedCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        dataManager = DataManager.getInstance();
        
        initializeViews();
        setupToolbar();
        setupSpinners();
        setupRecyclerView();
        
        // Get category from intent if provided
        String category = getIntent().getStringExtra("category");
        if (category != null) {
            selectedCategory = category;
        }
        
        loadDevices();
    }

    private void initializeViews() {
        recyclerViewItems = findViewById(R.id.recyclerViewItems);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerPriceRange = findViewById(R.id.spinnerPriceRange);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Browse Items");
        }
    }

    private void setupSpinners() {
        // Category spinner
        List<String> categories = dataManager.getCategories();
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Set selected category if provided
        if (!selectedCategory.equals("All")) {
            int position = categories.indexOf(selectedCategory);
            if (position >= 0) {
                spinnerCategory.setSelection(position);
            }
        }

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categories.get(position);
                loadDevices();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Price range spinner
        String[] priceRanges = {"All Prices", "$0-$25", "$26-$50", "$51-$75", "$76+"};
        ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, priceRanges);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriceRange.setAdapter(priceAdapter);

        spinnerPriceRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterByPriceRange(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewItems.setLayoutManager(layoutManager);
        
        deviceAdapter = new DeviceAdapter(this);
        recyclerViewItems.setAdapter(deviceAdapter);
    }

    private void loadDevices() {
        if (selectedCategory.equals("All")) {
            currentDevices = dataManager.getAllDevices();
        } else {
            currentDevices = dataManager.getDevicesByCategory(selectedCategory);
        }
        
        deviceAdapter.updateDevices(currentDevices);
    }

    private void filterByPriceRange(int rangeIndex) {
        if (currentDevices == null) return;

        List<TechDevice> filteredDevices;
        
        switch (rangeIndex) {
            case 0: // All Prices
                filteredDevices = currentDevices;
                break;
            case 1: // $0-$25
                filteredDevices = filterDevicesByPrice(0, 25);
                break;
            case 2: // $26-$50
                filteredDevices = filterDevicesByPrice(26, 50);
                break;
            case 3: // $51-$75
                filteredDevices = filterDevicesByPrice(51, 75);
                break;
            case 4: // $76+
                filteredDevices = filterDevicesByPrice(76, Double.MAX_VALUE);
                break;
            default:
                filteredDevices = currentDevices;
        }
        
        deviceAdapter.updateDevices(filteredDevices);
    }

    private List<TechDevice> filterDevicesByPrice(double minPrice, double maxPrice) {
        return currentDevices.stream()
                .filter(device -> device.getPricePerDay() >= minPrice && device.getPricePerDay() <= maxPrice)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDeviceClick(TechDevice device) {
        Intent intent = new Intent(this, ItemDetailsActivity.class);
        intent.putExtra("deviceId", device.getId());
        startActivity(intent);
    }

    @Override
    public void onRentClick(TechDevice device) {
        Intent intent = new Intent(this, ItemDetailsActivity.class);
        intent.putExtra("deviceId", device.getId());
        intent.putExtra("autoRent", true);
        startActivity(intent);
    }
}
