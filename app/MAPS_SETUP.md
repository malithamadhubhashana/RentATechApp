# TechARent - Google Maps Setup

## Google Maps API Key Setup

To use the Google Maps features in TechARent, you need to obtain a Google Maps API key and configure it in your project.

### Step 1: Get Google Maps API Key

1. Go to the [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Enable the following APIs:
   - Maps SDK for Android
   - Geocoding API
   - Places API (optional, for enhanced location search)

4. Go to "Credentials" and create an API key
5. Restrict the API key to Android apps for security
6. Add your app's package name: `com.example.techarent`
7. Add your app's SHA-1 fingerprint

### Step 2: Get SHA-1 Fingerprint

Run this command in your project directory:
```bash
gradlew signingReport
```

Look for the SHA1 fingerprint under `Variant: debug` and `Config: debug`

### Step 3: Add API Key to Project

1. Open `app/src/main/AndroidManifest.xml`
2. Find this line:
   ```xml
   android:value="YOUR_GOOGLE_MAPS_API_KEY_HERE"
   ```
3. Replace `YOUR_GOOGLE_MAPS_API_KEY_HERE` with your actual API key

### Step 4: Test the Integration

1. Build and run the app
2. Create a user account and login
3. Use the "Map" button in the top menu to view item locations
4. Use the "Add Item" feature to test GPS location auto-fill
5. Use "Select on Map" to choose locations on an interactive map

## Features Included

### Maps Integration:
- ✅ Interactive map showing all rental item locations
- ✅ Custom markers for different item categories
- ✅ Current location detection and display
- ✅ Click markers to view item details
- ✅ Floating action buttons for easy navigation

### GPS & Location Services:
- ✅ Automatic GPS location detection
- ✅ Auto-fill location in Add Item form
- ✅ Interactive map location selection
- ✅ Geocoding (coordinates to address conversion)
- ✅ Real-time location updates

### Enhanced Add Item Flow:
- ✅ GPS auto-fill with address lookup
- ✅ Interactive map location picker
- ✅ Location validation and error handling
- ✅ Improved location accuracy using FusedLocationProviderClient

## Sample Data

The app includes sample listings with real New York City coordinates:
- MacBook Pro 16" - Manhattan
- iPhone 15 Pro - Brooklyn  
- iPad Air - Queens
- Canon EOS R6 - Bronx
- PlayStation 5 - Staten Island
- AirPods Pro - Times Square
- Surface Pro 9 - Central Park
- Nintendo Switch - Wall Street

## Security Notes

- API key is properly configured in AndroidManifest.xml
- Location permissions are properly requested
- Database includes secure password hashing
- Input validation for all form fields

## Troubleshooting

1. **Maps not loading**: Check API key configuration and enabled APIs
2. **Location not working**: Ensure location permissions are granted
3. **Geocoding fails**: Check internet connection and Geocoding API enabled
4. **Build errors**: Ensure Google Play Services are up to date
