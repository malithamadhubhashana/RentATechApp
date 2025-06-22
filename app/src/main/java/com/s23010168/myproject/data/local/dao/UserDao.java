package com.s23010168.myproject.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.s23010168.myproject.data.local.entity.UserEntity;

import java.util.List;

@Dao
public interface UserDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity user);
    
    @Update
    void updateUser(UserEntity user);
    
    @Delete
    void deleteUser(UserEntity user);
    
    @Query("SELECT * FROM users WHERE id = :userId")
    LiveData<UserEntity> getUserById(String userId);
    
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    UserEntity getUserByEmail(String email);
    
    @Query("SELECT * FROM users WHERE email = :email AND id = :password LIMIT 1")
    UserEntity loginUser(String email, String password);
    
    @Query("SELECT * FROM users ORDER BY lastUpdated DESC")
    LiveData<List<UserEntity>> getAllUsers();
    
    @Query("SELECT * FROM users WHERE userType = :userType")
    LiveData<List<UserEntity>> getUsersByType(String userType);
    
    @Query("DELETE FROM users")
    void deleteAllUsers();
    
    @Query("SELECT COUNT(*) FROM users")
    int getUserCount();
    
    @Query("SELECT * FROM users WHERE name LIKE '%' || :searchQuery || '%' OR email LIKE '%' || :searchQuery || '%'")
    LiveData<List<UserEntity>> searchUsers(String searchQuery);
} 