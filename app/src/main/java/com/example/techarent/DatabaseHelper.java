package com.example.techarent;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "rentatech.db";
    private static final int DATABASE_VERSION = 1;
    
    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASSWORD_HASH = "password_hash";
    private static final String COLUMN_CREATED_DATE = "created_date";
    
    // Listings table
    private static final String TABLE_LISTINGS = "listings";
    private static final String COLUMN_LISTING_ID = "listing_id";
    private static final String COLUMN_OWNER_ID = "owner_id";
    private static final String COLUMN_ITEM_NAME = "item_name";
    private static final String COLUMN_BRAND = "brand";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE_PER_DAY = "price_per_day";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_IS_AVAILABLE = "is_available";
    
    // Bookings table
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String COLUMN_BOOKING_ID = "booking_id";
    private static final String COLUMN_RENTER_ID = "renter_id";
    private static final String COLUMN_START_DATE = "start_date";
    private static final String COLUMN_END_DATE = "end_date";
    private static final String COLUMN_TOTAL_COST = "total_cost";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_BOOKING_DATE = "booking_date";
    
    private Context context;
    private static final String PREF_NAME = "rentatech_prefs";
    private static final String KEY_LOGGED_IN_USER = "logged_in_user";
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FULL_NAME + " TEXT NOT NULL, " +
                COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_PASSWORD_HASH + " TEXT NOT NULL, " +
                COLUMN_CREATED_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP)";
        
        // Create Listings table
        String createListingsTable = "CREATE TABLE " + TABLE_LISTINGS + " (" +
                COLUMN_LISTING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_OWNER_ID + " INTEGER NOT NULL, " +
                COLUMN_ITEM_NAME + " TEXT NOT NULL, " +
                COLUMN_BRAND + " TEXT, " +
                COLUMN_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PRICE_PER_DAY + " REAL NOT NULL, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL, " +
                COLUMN_IS_AVAILABLE + " INTEGER DEFAULT 1, " +
                COLUMN_CREATED_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(" + COLUMN_OWNER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
        
        // Create Bookings table
        String createBookingsTable = "CREATE TABLE " + TABLE_BOOKINGS + " (" +
                COLUMN_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LISTING_ID + " INTEGER NOT NULL, " +
                COLUMN_RENTER_ID + " INTEGER NOT NULL, " +
                COLUMN_START_DATE + " TEXT NOT NULL, " +
                COLUMN_END_DATE + " TEXT NOT NULL, " +
                COLUMN_TOTAL_COST + " REAL NOT NULL, " +
                COLUMN_STATUS + " TEXT DEFAULT 'active', " +
                COLUMN_BOOKING_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(" + COLUMN_LISTING_ID + ") REFERENCES " + TABLE_LISTINGS + "(" + COLUMN_LISTING_ID + "), " +
                "FOREIGN KEY(" + COLUMN_RENTER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
        
        db.execSQL(createUsersTable);
        db.execSQL(createListingsTable);
        db.execSQL(createBookingsTable);
        
        // Insert sample data
        insertSampleData(db);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
    
    // User operations
    public long createUser(String fullName, String email, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_PASSWORD_HASH, hashPassword(password));
        values.put(COLUMN_CREATED_DATE, getCurrentTimestamp());
        
        long userId = db.insert(TABLE_USERS, null, values);
        db.close();
        return userId;
    }
    
    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedPassword = hashPassword(password);
        
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD_HASH + "=?",
                new String[]{email, hashedPassword},
                null, null, null);
        
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }
    
    public boolean emailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);
        
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
    
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        
        Cursor cursor = db.query(TABLE_USERS,
                null,
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);
        
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
            user.setFullName(cursor.getString(cursor.getColumnIndex(COLUMN_FULL_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
        }
        
        cursor.close();
        db.close();
        return user;
    }
    
    // Listing operations
    public long createListing(int ownerId, String itemName, String brand, String category, 
                             String description, double pricePerDay, String location, 
                             double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_OWNER_ID, ownerId);
        values.put(COLUMN_ITEM_NAME, itemName);
        values.put(COLUMN_BRAND, brand);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PRICE_PER_DAY, pricePerDay);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_CREATED_DATE, getCurrentTimestamp());
        
        long listingId = db.insert(TABLE_LISTINGS, null, values);
        db.close();
        return listingId;
    }
    
    public List<Listing> getAllListings() {
        List<Listing> listings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String query = "SELECT l.*, u." + COLUMN_FULL_NAME + " as owner_name " +
                      "FROM " + TABLE_LISTINGS + " l " +
                      "JOIN " + TABLE_USERS + " u ON l." + COLUMN_OWNER_ID + " = u." + COLUMN_USER_ID +
                      " WHERE " + COLUMN_IS_AVAILABLE + " = 1 " +
                      "ORDER BY l." + COLUMN_CREATED_DATE + " DESC";
        
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst()) {
            do {
                Listing listing = cursorToListing(cursor);
                listings.add(listing);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return listings;
    }
    
    public List<Listing> getListingsByCategory(String category) {
        List<Listing> listings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String query = "SELECT l.*, u." + COLUMN_FULL_NAME + " as owner_name " +
                      "FROM " + TABLE_LISTINGS + " l " +
                      "JOIN " + TABLE_USERS + " u ON l." + COLUMN_OWNER_ID + " = u." + COLUMN_USER_ID +
                      " WHERE l." + COLUMN_CATEGORY + " = ? AND " + COLUMN_IS_AVAILABLE + " = 1 " +
                      "ORDER BY l." + COLUMN_CREATED_DATE + " DESC";
        
        Cursor cursor = db.rawQuery(query, new String[]{category});
        
        if (cursor.moveToFirst()) {
            do {
                Listing listing = cursorToListing(cursor);
                listings.add(listing);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return listings;
    }
    
    // Booking operations
    public long createBooking(int listingId, int renterId, String startDate, String endDate, double totalCost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_LISTING_ID, listingId);
        values.put(COLUMN_RENTER_ID, renterId);
        values.put(COLUMN_START_DATE, startDate);
        values.put(COLUMN_END_DATE, endDate);
        values.put(COLUMN_TOTAL_COST, totalCost);
        values.put(COLUMN_BOOKING_DATE, getCurrentTimestamp());
        
        long bookingId = db.insert(TABLE_BOOKINGS, null, values);
        db.close();
        return bookingId;
    }
    
    // Session management
    public void setUserLoggedIn(String email) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_LOGGED_IN_USER, email).apply();
    }
    
    public String getLoggedInUserEmail() {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LOGGED_IN_USER, null);
    }
    
    public boolean isUserLoggedIn() {
        return getLoggedInUserEmail() != null;
    }
    
    public void logout() {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_LOGGED_IN_USER).apply();
    }
    
    // Helper methods
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e("DatabaseHelper", "Error hashing password", e);
            return password; // Fallback to plain text (not recommended for production)
        }
    }
    
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    
    private Listing cursorToListing(Cursor cursor) {
        Listing listing = new Listing();
        listing.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_LISTING_ID)));
        listing.setOwnerId(cursor.getInt(cursor.getColumnIndex(COLUMN_OWNER_ID)));
        listing.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME)));
        listing.setBrand(cursor.getString(cursor.getColumnIndex(COLUMN_BRAND)));
        listing.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
        listing.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
        listing.setPricePerDay(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE_PER_DAY)));
        listing.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)));
        listing.setLatitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE)));
        listing.setLongitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE)));
        listing.setAvailable(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_AVAILABLE)) == 1);
        
        int ownerNameIndex = cursor.getColumnIndex("owner_name");
        if (ownerNameIndex != -1) {
            listing.setOwnerName(cursor.getString(ownerNameIndex));
        }
        
        return listing;
    }
    
    private void insertSampleData(SQLiteDatabase db) {
        // Create a sample user (owner of sample listings)
        ContentValues userValues = new ContentValues();
        userValues.put(COLUMN_FULL_NAME, "Demo User");
        userValues.put(COLUMN_EMAIL, "demo@rentatech.com");
        userValues.put(COLUMN_PHONE, "+1-555-DEMO");
        userValues.put(COLUMN_PASSWORD_HASH, hashPassword("demo123"));
        userValues.put(COLUMN_CREATED_DATE, getCurrentTimestamp());
        
        long demoUserId = db.insert(TABLE_USERS, null, userValues);
        
        // Sample listings with real coordinates (New York City area)
        insertSampleListing(db, (int)demoUserId, "MacBook Pro 16\"", "Apple", "Laptops", 
            "Latest MacBook Pro with M2 Pro chip, perfect for professional work", 89.99,
            "Manhattan, New York", 40.7580, -73.9855);
        
        insertSampleListing(db, (int)demoUserId, "iPhone 15 Pro", "Apple", "Smartphones", 
            "Latest iPhone with advanced camera system", 45.99,
            "Brooklyn, New York", 40.6782, -73.9442);
        
        insertSampleListing(db, (int)demoUserId, "iPad Air", "Apple", "Tablets", 
            "Perfect for creativity and productivity", 35.99,
            "Queens, New York", 40.7282, -73.7949);
        
        insertSampleListing(db, (int)demoUserId, "Canon EOS R6", "Canon", "Cameras", 
            "Professional mirrorless camera for photography enthusiasts", 75.99,
            "Bronx, New York", 40.8448, -73.8648);
        
        insertSampleListing(db, (int)demoUserId, "PlayStation 5", "Sony", "Gaming", 
            "Latest gaming console with 4K gaming support", 25.99,
            "Staten Island, New York", 40.5795, -74.1502);
        
        insertSampleListing(db, (int)demoUserId, "AirPods Pro", "Apple", "Audio", 
            "Premium wireless earbuds with noise cancellation", 15.99,
            "Times Square, New York", 40.7589, -73.9851);
        
        insertSampleListing(db, (int)demoUserId, "Surface Pro 9", "Microsoft", "Tablets", 
            "2-in-1 laptop and tablet for productivity", 55.99,
            "Central Park, New York", 40.7829, -73.9654);
        
        insertSampleListing(db, (int)demoUserId, "Nintendo Switch", "Nintendo", "Gaming", 
            "Portable gaming console perfect for on-the-go gaming", 20.99,
            "Wall Street, New York", 40.7074, -74.0113);
    }
    
    private void insertSampleListing(SQLiteDatabase db, int ownerId, String itemName, String brand, 
                                   String category, String description, double pricePerDay, 
                                   String location, double latitude, double longitude) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_OWNER_ID, ownerId);
        values.put(COLUMN_ITEM_NAME, itemName);
        values.put(COLUMN_BRAND, brand);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PRICE_PER_DAY, pricePerDay);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_CREATED_DATE, getCurrentTimestamp());
        
        db.insert(TABLE_LISTINGS, null, values);
    }

    // Method to get user by ID
    public Cursor getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, COLUMN_USER_ID + "=?", 
                       new String[]{String.valueOf(userId)}, null, null, null);
    }

    // Method to get count of user's listings
    public int getUserListingsCount(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_LISTINGS + 
                                   " WHERE " + COLUMN_OWNER_ID + "=?", 
                                   new String[]{String.valueOf(userId)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // Method to get count of user's bookings
    public int getUserBookingsCount(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_BOOKINGS + 
                                   " WHERE " + COLUMN_RENTER_ID + "=?", 
                                   new String[]{String.valueOf(userId)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
    
    // Get user's listed items
    public android.database.Cursor getUserListedItems(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        // First get the user ID
        String getUserIdQuery = "SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USERS + 
                               " WHERE " + COLUMN_EMAIL + " = ?";
        android.database.Cursor userCursor = db.rawQuery(getUserIdQuery, new String[]{userEmail});
        
        if (!userCursor.moveToFirst()) {
            userCursor.close();
            return null;
        }
        
        int userId = userCursor.getInt(0);
        userCursor.close();
        
        // Get listings for this user
        String query = "SELECT * FROM " + TABLE_LISTINGS + 
                      " WHERE " + COLUMN_OWNER_ID + " = ? " +
                      " ORDER BY " + COLUMN_CREATED_DATE + " DESC";
        
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }
    
    // Get count of user's listed items
    public int getUserListedItemsCount(String userEmail) {
        android.database.Cursor cursor = getUserListedItems(userEmail);
        if (cursor == null) return 0;
        
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
