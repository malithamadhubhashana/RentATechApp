package com.s23010168.myproject.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.s23010168.myproject.data.local.dao.ListingDao;
import com.s23010168.myproject.data.local.dao.UserDao;
import com.s23010168.myproject.data.local.entity.ListingEntity;
import com.s23010168.myproject.data.local.entity.UserEntity;

@Database(
    entities = {UserEntity.class, ListingEntity.class},
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "rental_app_db";
    private static volatile AppDatabase INSTANCE;
    
    public abstract UserDao userDao();
    public abstract ListingDao listingDao();
    
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
} 