package com.example.techarent;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.techarent.data.DataManager;
import com.example.techarent.model.Rental;
import com.example.techarent.model.TechDevice;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ItemDetailsActivity extends AppCompatActivity {

    private TechDevice device;
    private DataManager dataManager;
    
    private TextView tvItemImagePlaceholder, tvItemName, tvItemBrand, tvItemPrice,
                    tvAvailabilityStatus, tvItemDescription, tvTotalCost;
    private View viewAvailabilityIndicator;
    private EditText etStartDate, etEndDate;
    private Button btnConfirmRental;
    
    private Date selectedStartDate, selectedEndDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        dataManager = DataManager.getInstance();
        
        String deviceId = getIntent().getStringExtra("deviceId");
        if (deviceId != null) {
            device = dataManager.getDeviceById(deviceId);
        }
        
        if (device == null) {
            Toast.makeText(this, "Device not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupToolbar();
        populateDeviceDetails();
        setupDatePickers();
        setupRentalButton();
    }

    private void initializeViews() {
        tvItemImagePlaceholder = findViewById(R.id.tvItemImagePlaceholder);
        tvItemName = findViewById(R.id.tvItemName);
        tvItemBrand = findViewById(R.id.tvItemBrand);
        tvItemPrice = findViewById(R.id.tvItemPrice);
        tvAvailabilityStatus = findViewById(R.id.tvAvailabilityStatus);
        viewAvailabilityIndicator = findViewById(R.id.viewAvailabilityIndicator);
        tvItemDescription = findViewById(R.id.tvItemDescription);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        tvTotalCost = findViewById(R.id.tvTotalCost);
        btnConfirmRental = findViewById(R.id.btnConfirmRental);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Item Details");
        }
    }

    private void populateDeviceDetails() {
        tvItemImagePlaceholder.setText(device.getCategoryEmoji());
        tvItemName.setText(device.getName());
        tvItemBrand.setText(device.getBrand());
        tvItemPrice.setText(String.format(Locale.getDefault(), "$%.0f", device.getPricePerDay()));
        tvItemDescription.setText(device.getDescription());

        // Set availability status
        if (device.isAvailable()) {
            tvAvailabilityStatus.setText(R.string.available);
            tvAvailabilityStatus.setTextColor(getColor(R.color.accent_green));
            viewAvailabilityIndicator.setBackgroundResource(R.drawable.circle_green);
        } else {
            tvAvailabilityStatus.setText(R.string.unavailable);
            tvAvailabilityStatus.setTextColor(getColor(R.color.accent_red));
            viewAvailabilityIndicator.setBackgroundResource(R.drawable.circle_red);
            btnConfirmRental.setEnabled(false);
            btnConfirmRental.setText("Unavailable");
        }
    }

    private void setupDatePickers() {
        Calendar calendar = Calendar.getInstance();
        
        etStartDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    selectedStartDate = calendar.getTime();
                    etStartDate.setText(dateFormat.format(selectedStartDate));
                    calculateTotalCost();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            );
            
            // Set minimum date to today
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        etEndDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    selectedEndDate = calendar.getTime();
                    etEndDate.setText(dateFormat.format(selectedEndDate));
                    calculateTotalCost();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            );
            
            // Set minimum date to start date or today
            long minDate = selectedStartDate != null ? selectedStartDate.getTime() : System.currentTimeMillis();
            datePickerDialog.getDatePicker().setMinDate(minDate);
            datePickerDialog.show();
        });
    }

    private void calculateTotalCost() {
        if (selectedStartDate != null && selectedEndDate != null) {
            long diffInMillis = selectedEndDate.getTime() - selectedStartDate.getTime();
            long days = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            
            if (days < 0) {
                tvTotalCost.setText("$0");
                Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Add 1 to include both start and end dates
            days += 1;
            double totalCost = days * device.getPricePerDay();
            tvTotalCost.setText(String.format(Locale.getDefault(), "$%.0f", totalCost));
        }
    }

    private void setupRentalButton() {
        btnConfirmRental.setOnClickListener(v -> {
            if (!device.isAvailable()) {
                Toast.makeText(this, "This item is currently unavailable", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (selectedStartDate == null || selectedEndDate == null) {
                Toast.makeText(this, "Please select rental dates", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (selectedEndDate.before(selectedStartDate)) {
                Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
                return;
            }
            
            confirmRental();
        });
    }

    private void confirmRental() {
        // Calculate rental details
        long diffInMillis = selectedEndDate.getTime() - selectedStartDate.getTime();
        long days = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) + 1;
        double totalCost = days * device.getPricePerDay();
        
        // Create rental
        String rentalId = "r" + System.currentTimeMillis();
        Rental rental = new Rental(
            rentalId,
            device.getId(),
            device.getName(),
            device.getCategory(),
            selectedStartDate,
            selectedEndDate,
            totalCost,
            Rental.RentalStatus.ACTIVE
        );
        
        // Add to data manager
        dataManager.addRental(rental);
        
        Toast.makeText(this, "Rental confirmed! Total: $" + String.format("%.0f", totalCost), 
                      Toast.LENGTH_LONG).show();
        
        // Return to previous screen
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
