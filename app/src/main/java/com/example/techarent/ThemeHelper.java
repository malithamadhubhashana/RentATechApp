package com.example.techarent;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeHelper {
    
    private static final String THEME_PREFERENCE = "theme_preference";
    private static final int THEME_LIGHT = 1;
    private static final int THEME_DARK = 2;
    private static final int THEME_SYSTEM = 0;
    
    /**
     * Apply the saved theme preference
     */
    public static void applyTheme(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("RentATech", Context.MODE_PRIVATE);
        int savedTheme = preferences.getInt(THEME_PREFERENCE, THEME_SYSTEM);
        
        int nightMode;
        switch (savedTheme) {
            case THEME_LIGHT:
                nightMode = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case THEME_DARK:
                nightMode = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            case THEME_SYSTEM:
            default:
                nightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                break;
        }
        
        AppCompatDelegate.setDefaultNightMode(nightMode);
    }
    
    /**
     * Save theme preference
     */
    public static void saveTheme(Context context, int theme) {
        SharedPreferences preferences = context.getSharedPreferences("RentATech", Context.MODE_PRIVATE);
        preferences.edit().putInt(THEME_PREFERENCE, theme).apply();
    }
    
    /**
     * Get current theme preference
     */
    public static int getCurrentTheme(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("RentATech", Context.MODE_PRIVATE);
        return preferences.getInt(THEME_PREFERENCE, THEME_SYSTEM);
    }
    
    /**
     * Check if current theme is dark
     */
    public static boolean isDarkTheme(Context context) {
        int currentTheme = getCurrentTheme(context);
        if (currentTheme == THEME_DARK) {
            return true;
        } else if (currentTheme == THEME_LIGHT) {
            return false;
        } else {
            // For system theme, check current configuration
            int currentNightMode = context.getResources().getConfiguration().uiMode 
                    & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
            return currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES;
        }
    }
    
    // Theme constants for external use
    public static final int LIGHT_THEME = THEME_LIGHT;
    public static final int DARK_THEME = THEME_DARK;
    public static final int SYSTEM_THEME = THEME_SYSTEM;
}
