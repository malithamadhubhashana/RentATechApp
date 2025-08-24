package com.example.techarent;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Diagnostic activity to show available sensors and their status
 */
public class SensorDiagnosticsActivity extends AppCompatActivity {
    
    private TextView tvSensorInfo;
    private TextView tvBatteryLevel;
    private SensorManager sensorManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_diagnostics);
        
        initializeViews();
        setupSensorManager();
        displaySensorInfo();
    }
    
    private void initializeViews() {
        tvSensorInfo = findViewById(R.id.tv_sensor_info);
        tvBatteryLevel = findViewById(R.id.tv_battery_level);
        
        // Set up toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sensor Diagnostics");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void setupSensorManager() {
        sensorManager = new SensorManager(this, new SensorManager.SensorListener() {
            @Override
            public void onBatteryLevelChanged(int level) {
                runOnUiThread(() -> 
                    tvBatteryLevel.setText("Battery Level: " + level + "%"));
            }
            
            @Override
            public void onDeviceShaken() {
                // Handle shake in diagnostics if needed
            }
            
            @Override
            public void onDeviceDropped() {
                // Handle drop detection in diagnostics if needed
            }
        });
    }
    
    private void displaySensorInfo() {
        // Display available sensors
        String sensorInfo = sensorManager.getAvailableSensors();
        tvSensorInfo.setText(sensorInfo);
        
        // Display battery level
        int batteryLevel = sensorManager.getBatteryLevel();
        tvBatteryLevel.setText("Battery Level: " + batteryLevel + "%");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensorManager.startMonitoring();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.stopMonitoring();
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
