package com.example.cloudservices.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cloudservices.data.model.WeatherEntity

@Database(entities = [WeatherEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
