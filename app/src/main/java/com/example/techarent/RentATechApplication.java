package com.example.techarent;

import android.app.Application;

public class RentATechApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Apply saved theme preference on app startup
        ThemeHelper.applyTheme(this);
    }
}
