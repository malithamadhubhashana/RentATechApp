# Google Maps Setup Instructions

## Why is the map not showing?

The map functionality requires a valid Google Maps API key. The app is built and ready to use, but you need to configure the API key first.

## How to set up Google Maps API Key:

### Step 1: Get a Google Maps API Key
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Enable the "Maps SDK for Android" API
4. Go to "Credentials" and create an API key
5. Restrict the API key to Android apps (optional but recommended)

### Step 2: Add the API Key to your app
1. Open `app/src/main/AndroidManifest.xml`
2. Find this line:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="YOUR_GOOGLE_MAPS_API_KEY_HERE" />
   ```
3. Replace `YOUR_GOOGLE_MAPS_API_KEY_HERE` with your actual API key

### Step 3: Test the app
After adding the API key:
1. Build and run the app
2. Tap the map icon in the toolbar
3. You should see the map with sample rental items marked

## App Features Added:

### ✅ Profile Page
- View user information (name, email, phone)
- See statistics (items listed, rentals made)
- Profile options (edit profile, settings, help)
- Proper logout functionality

### ✅ Fixed Logout Option
- Clears user session properly
- Shows confirmation message
- Redirects to welcome screen
- Prevents back navigation to authenticated screens

### ✅ Map Integration (Ready)
- Google Maps SDK integrated
- GPS location services
- Interactive map with rental item markers
- Location selection for new listings
- Automatic address geocoding

## Navigation:
- **Profile**: Tap the profile icon in the top toolbar
- **Logout**: Available in both main menu and profile page
- **Map**: Tap the map icon (requires API key setup)

## Test Credentials:
- Email: demo@techarent.com
- Password: demo123

## Sample Data:
The app includes 8 sample rental items with real New York City coordinates for testing the map functionality.
