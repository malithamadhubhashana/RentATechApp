package com.example.techarent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {
    
    private RadioGroup themeRadioGroup;
    private RadioButton rbLight, rbDark, rbSystem;
    private SharedPreferences preferences;
    
    private static final String THEME_PREFERENCE = "theme_preference";
    private static final int THEME_LIGHT = 1;
    private static final int THEME_DARK = 2;
    private static final int THEME_SYSTEM = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        // Setup action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        // Initialize preferences
        preferences = getSharedPreferences("RentATech", MODE_PRIVATE);
        
        // Initialize views
        initViews();
        
        // Load current theme selection
        loadThemeSelection();
        
        // Set listeners
        setThemeChangeListener();
    }
    
    private void initViews() {
        themeRadioGroup = findViewById(R.id.theme_radio_group);
        rbLight = findViewById(R.id.rb_light_theme);
        rbDark = findViewById(R.id.rb_dark_theme);
        rbSystem = findViewById(R.id.rb_system_theme);
    }
    
    private void loadThemeSelection() {
        int currentTheme = preferences.getInt(THEME_PREFERENCE, THEME_SYSTEM);
        
        switch (currentTheme) {
            case THEME_LIGHT:
                rbLight.setChecked(true);
                break;
            case THEME_DARK:
                rbDark.setChecked(true);
                break;
            case THEME_SYSTEM:
            default:
                rbSystem.setChecked(true);
                break;
        }
    }
    
    private void setThemeChangeListener() {
        themeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedTheme;
            int nightMode;
            
            if (checkedId == R.id.rb_light_theme) {
                selectedTheme = THEME_LIGHT;
                nightMode = AppCompatDelegate.MODE_NIGHT_NO;
            } else if (checkedId == R.id.rb_dark_theme) {
                selectedTheme = THEME_DARK;
                nightMode = AppCompatDelegate.MODE_NIGHT_YES;
            } else {
                selectedTheme = THEME_SYSTEM;
                nightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            }
            
            // Save preference
            preferences.edit().putInt(THEME_PREFERENCE, selectedTheme).apply();
            
            // Apply theme
            AppCompatDelegate.setDefaultNightMode(nightMode);
        });
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
