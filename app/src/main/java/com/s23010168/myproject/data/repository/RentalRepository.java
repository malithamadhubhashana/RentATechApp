package com.s23010168.myproject.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.s23010168.myproject.data.local.AppDatabase;
import com.s23010168.myproject.data.local.dao.ListingDao;
import com.s23010168.myproject.data.local.dao.UserDao;
import com.s23010168.myproject.data.local.entity.ListingEntity;
import com.s23010168.myproject.data.local.entity.UserEntity;
import com.s23010168.myproject.models.Listing;
import com.s23010168.myproject.models.User;

import java.util.List;
import java.util.UUID;

public class RentalRepository {
    
    private UserDao userDao;
    private ListingDao listingDao;
    private LiveData<List<UserEntity>> allUsers;
    private LiveData<List<ListingEntity>> allListings;
    
    public RentalRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        userDao = database.userDao();
        listingDao = database.listingDao();
        allUsers = userDao.getAllUsers();
        allListings = listingDao.getAllListings();
    }
    
    // User operations
    public void insertUser(User user) {
        UserEntity userEntity = convertUserToEntity(user);
        new InsertUserAsyncTask(userDao).execute(userEntity);
    }
    
    public void updateUser(User user) {
        UserEntity userEntity = convertUserToEntity(user);
        new UpdateUserAsyncTask(userDao).execute(userEntity);
    }
    
    public void deleteUser(User user) {
        UserEntity userEntity = convertUserToEntity(user);
        new DeleteUserAsyncTask(userDao).execute(userEntity);
    }
    
    public LiveData<UserEntity> getUserById(String userId) {
        return userDao.getUserById(userId);
    }
    
    public UserEntity getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }
    
    public UserEntity loginUser(String email, String password) {
        return userDao.loginUser(email, password);
    }
    
    public LiveData<List<UserEntity>> getAllUsers() {
        return allUsers;
    }
    
    public LiveData<List<UserEntity>> getUsersByType(String userType) {
        return userDao.getUsersByType(userType);
    }
    
    public LiveData<List<UserEntity>> searchUsers(String searchQuery) {
        return userDao.searchUsers(searchQuery);
    }
    
    // Listing operations
    public void insertListing(Listing listing) {
        ListingEntity listingEntity = convertListingToEntity(listing);
        new InsertListingAsyncTask(listingDao).execute(listingEntity);
    }
    
    public void updateListing(Listing listing) {
        ListingEntity listingEntity = convertListingToEntity(listing);
        new UpdateListingAsyncTask(listingDao).execute(listingEntity);
    }
    
    public void deleteListing(Listing listing) {
        ListingEntity listingEntity = convertListingToEntity(listing);
        new DeleteListingAsyncTask(listingDao).execute(listingEntity);
    }
    
    public LiveData<ListingEntity> getListingById(String listingId) {
        return listingDao.getListingById(listingId);
    }
    
    public LiveData<List<ListingEntity>> getAllListings() {
        return allListings;
    }
    
    public LiveData<List<ListingEntity>> getAvailableListings() {
        return listingDao.getAvailableListings();
    }
    
    public LiveData<List<ListingEntity>> getListingsByOwner(String ownerId) {
        return listingDao.getListingsByOwner(ownerId);
    }
    
    public LiveData<List<ListingEntity>> getListingsByCategory(String category) {
        return listingDao.getListingsByCategory(category);
    }
    
    public LiveData<List<ListingEntity>> getListingsByPriceRange(double minPrice, double maxPrice) {
        return listingDao.getListingsByPriceRange(minPrice, maxPrice);
    }
    
    public LiveData<List<ListingEntity>> getListingsByLocation(String location) {
        return listingDao.getListingsByLocation(location);
    }
    
    public LiveData<List<ListingEntity>> searchListings(String searchQuery) {
        return listingDao.searchListings(searchQuery);
    }
    
    public LiveData<List<ListingEntity>> getTopRatedListings() {
        return listingDao.getTopRatedListings();
    }
    
    public LiveData<List<ListingEntity>> getRecentListings() {
        return listingDao.getRecentListings();
    }
    
    public LiveData<List<String>> getAllCategories() {
        return listingDao.getAllCategories();
    }
    
    public LiveData<List<String>> getAllLocations() {
        return listingDao.getAllLocations();
    }
    
    // Conversion methods
    private UserEntity convertUserToEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId() != null ? user.getId() : UUID.randomUUID().toString());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setProfileImageUrl(user.getProfileImageUrl());
        entity.setBio(user.getBio());
        entity.setUserType(user.getUserType() != null ? user.getUserType().name() : "RENTER");
        entity.setPhoneNumber(user.getPhoneNumber());
        entity.setLocation(user.getLocation());
        entity.setListingsCount(user.getListingsCount());
        entity.setRating(user.getRating());
        entity.setReviewCount(user.getReviewCount());
        entity.setLastUpdated(System.currentTimeMillis());
        return entity;
    }
    
    private ListingEntity convertListingToEntity(Listing listing) {
        ListingEntity entity = new ListingEntity();
        entity.setId(listing.getId() != null ? listing.getId() : UUID.randomUUID().toString());
        entity.setTitle(listing.getTitle());
        entity.setDescription(listing.getDescription());
        entity.setPrice(listing.getPrice());
        entity.setLocation(listing.getLocation());
        entity.setCategory(listing.getCategory());
        // Convert List<String> to JSON string for storage
        if (listing.getImageUrls() != null) {
            entity.setImageUrls(String.join(",", listing.getImageUrls()));
        }
        entity.setOwnerId(listing.getOwnerId());
        entity.setOwnerName(listing.getOwnerName());
        entity.setOwnerImageUrl(listing.getOwnerImageUrl());
        entity.setAvailable(listing.isAvailable());
        entity.setRating(listing.getRating());
        entity.setReviewCount(listing.getReviewCount());
        entity.setCreatedAt(listing.getCreatedAt());
        entity.setLatitude(listing.getLatitude());
        entity.setLongitude(listing.getLongitude());
        // Convert List<String> to JSON string for storage
        if (listing.getAmenities() != null) {
            entity.setAmenities(String.join(",", listing.getAmenities()));
        }
        entity.setMaxGuests(listing.getMaxGuests());
        entity.setBedrooms(listing.getBedrooms());
        entity.setBathrooms(listing.getBathrooms());
        entity.setLastUpdated(System.currentTimeMillis());
        return entity;
    }
    
    // AsyncTask classes for database operations
    private static class InsertUserAsyncTask extends AsyncTask<UserEntity, Void, Void> {
        private UserDao userDao;
        
        InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        
        @Override
        protected Void doInBackground(UserEntity... userEntities) {
            userDao.insertUser(userEntities[0]);
            return null;
        }
    }
    
    private static class UpdateUserAsyncTask extends AsyncTask<UserEntity, Void, Void> {
        private UserDao userDao;
        
        UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        
        @Override
        protected Void doInBackground(UserEntity... userEntities) {
            userDao.updateUser(userEntities[0]);
            return null;
        }
    }
    
    private static class DeleteUserAsyncTask extends AsyncTask<UserEntity, Void, Void> {
        private UserDao userDao;
        
        DeleteUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        
        @Override
        protected Void doInBackground(UserEntity... userEntities) {
            userDao.deleteUser(userEntities[0]);
            return null;
        }
    }
    
    private static class InsertListingAsyncTask extends AsyncTask<ListingEntity, Void, Void> {
        private ListingDao listingDao;
        
        InsertListingAsyncTask(ListingDao listingDao) {
            this.listingDao = listingDao;
        }
        
        @Override
        protected Void doInBackground(ListingEntity... listingEntities) {
            listingDao.insertListing(listingEntities[0]);
            return null;
        }
    }
    
    private static class UpdateListingAsyncTask extends AsyncTask<ListingEntity, Void, Void> {
        private ListingDao listingDao;
        
        UpdateListingAsyncTask(ListingDao listingDao) {
            this.listingDao = listingDao;
        }
        
        @Override
        protected Void doInBackground(ListingEntity... listingEntities) {
            listingDao.updateListing(listingEntities[0]);
            return null;
        }
    }
    
    private static class DeleteListingAsyncTask extends AsyncTask<ListingEntity, Void, Void> {
        private ListingDao listingDao;
        
        DeleteListingAsyncTask(ListingDao listingDao) {
            this.listingDao = listingDao;
        }
        
        @Override
        protected Void doInBackground(ListingEntity... listingEntities) {
            listingDao.deleteListing(listingEntities[0]);
            return null;
        }
    }
} 