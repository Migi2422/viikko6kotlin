package com.example.cloudservices.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cloudservices.data.model.WeatherEntity

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather WHERE city = :city LIMIT 1")
    suspend fun getWeather(city: String): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(entity: WeatherEntity)
}
