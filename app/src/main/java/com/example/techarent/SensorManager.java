package com.example.techarent;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.BatteryManager;

/**
 * Sensor management utility for RentATech app
 * Handles device sensors for enhanced rental experience
 */
public class SensorManager implements SensorEventListener {
    
    private Context context;
    private android.hardware.SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorListener listener;
    
    // Interface for sensor callbacks
    public interface SensorListener {
        void onBatteryLevelChanged(int level);
        void onDeviceShaken();
        void onDeviceDropped();
    }
    
    public SensorManager(Context context, SensorListener listener) {
        this.context = context;
        this.listener = listener;
        this.sensorManager = (android.hardware.SensorManager) 
            context.getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    
    /**
     * Start monitoring sensors
     */
    public void startMonitoring() {
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, 
                android.hardware.SensorManager.SENSOR_DELAY_UI);
        }
    }
    
    /**
     * Stop monitoring sensors
     */
    public void stopMonitoring() {
        sensorManager.unregisterListener(this);
    }
    
    /**
     * Get current battery level
     */
    public int getBatteryLevel() {
        IntentFilter batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, batteryFilter);
        
        if (batteryStatus != null) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            return (int) ((level / (float) scale) * 100);
        }
        return -1;
    }
    
    /**
     * Check if device has specific sensor
     */
    public boolean hasSensor(int sensorType) {
        return sensorManager.getDefaultSensor(sensorType) != null;
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            
            // Calculate acceleration magnitude
            double acceleration = Math.sqrt(x*x + y*y + z*z);
            
            // Detect shake (strong movement)
            if (acceleration > 15) {
                if (listener != null) {
                    listener.onDeviceShaken();
                }
            }
            
            // Detect potential drop (sudden acceleration)
            if (acceleration > 25) {
                if (listener != null) {
                    listener.onDeviceDropped();
                }
            }
        }
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }
    
    /**
     * Get available sensors info for diagnostics
     */
    public String getAvailableSensors() {
        StringBuilder sensors = new StringBuilder();
        sensors.append("Available Sensors:\n");
        
        if (hasSensor(Sensor.TYPE_ACCELEROMETER)) sensors.append("✓ Accelerometer\n");
        if (hasSensor(Sensor.TYPE_GYROSCOPE)) sensors.append("✓ Gyroscope\n");
        if (hasSensor(Sensor.TYPE_MAGNETIC_FIELD)) sensors.append("✓ Magnetometer\n");
        if (hasSensor(Sensor.TYPE_PROXIMITY)) sensors.append("✓ Proximity\n");
        if (hasSensor(Sensor.TYPE_LIGHT)) sensors.append("✓ Light Sensor\n");
        
        return sensors.toString();
    }
}
