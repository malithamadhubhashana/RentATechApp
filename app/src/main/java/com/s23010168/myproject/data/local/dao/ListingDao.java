package com.s23010168.myproject.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.s23010168.myproject.data.local.entity.ListingEntity;

import java.util.List;

@Dao
public interface ListingDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListing(ListingEntity listing);
    
    @Update
    void updateListing(ListingEntity listing);
    
    @Delete
    void deleteListing(ListingEntity listing);
    
    @Query("SELECT * FROM listings WHERE id = :listingId")
    LiveData<ListingEntity> getListingById(String listingId);
    
    @Query("SELECT * FROM listings ORDER BY createdAt DESC")
    LiveData<List<ListingEntity>> getAllListings();
    
    @Query("SELECT * FROM listings WHERE isAvailable = 1 ORDER BY createdAt DESC")
    LiveData<List<ListingEntity>> getAvailableListings();
    
    @Query("SELECT * FROM listings WHERE ownerId = :ownerId ORDER BY createdAt DESC")
    LiveData<List<ListingEntity>> getListingsByOwner(String ownerId);
    
    @Query("SELECT * FROM listings WHERE category = :category AND isAvailable = 1")
    LiveData<List<ListingEntity>> getListingsByCategory(String category);
    
    @Query("SELECT * FROM listings WHERE price BETWEEN :minPrice AND :maxPrice AND isAvailable = 1")
    LiveData<List<ListingEntity>> getListingsByPriceRange(double minPrice, double maxPrice);
    
    @Query("SELECT * FROM listings WHERE location LIKE '%' || :location || '%' AND isAvailable = 1")
    LiveData<List<ListingEntity>> getListingsByLocation(String location);
    
    @Query("SELECT * FROM listings WHERE title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%' AND isAvailable = 1")
    LiveData<List<ListingEntity>> searchListings(String searchQuery);
    
    @Query("SELECT * FROM listings WHERE rating >= :minRating AND isAvailable = 1 ORDER BY rating DESC")
    LiveData<List<ListingEntity>> getListingsByRating(float minRating);
    
    @Query("SELECT * FROM listings WHERE bedrooms >= :minBedrooms AND isAvailable = 1")
    LiveData<List<ListingEntity>> getListingsByBedrooms(int minBedrooms);
    
    @Query("SELECT * FROM listings WHERE maxGuests >= :guests AND isAvailable = 1")
    LiveData<List<ListingEntity>> getListingsByGuestCount(int guests);
    
    @Query("SELECT * FROM listings WHERE isAvailable = 1 ORDER BY rating DESC LIMIT 10")
    LiveData<List<ListingEntity>> getTopRatedListings();
    
    @Query("SELECT * FROM listings WHERE isAvailable = 1 ORDER BY createdAt DESC LIMIT 10")
    LiveData<List<ListingEntity>> getRecentListings();
    
    @Query("DELETE FROM listings")
    void deleteAllListings();
    
    @Query("SELECT COUNT(*) FROM listings")
    int getListingCount();
    
    @Query("SELECT COUNT(*) FROM listings WHERE ownerId = :ownerId")
    int getListingCountByOwner(String ownerId);
    
    @Query("SELECT DISTINCT category FROM listings WHERE isAvailable = 1")
    LiveData<List<String>> getAllCategories();
    
    @Query("SELECT DISTINCT location FROM listings WHERE isAvailable = 1")
    LiveData<List<String>> getAllLocations();
} 