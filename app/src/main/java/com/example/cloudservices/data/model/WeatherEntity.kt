package com.example.cloudservices.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val city: String,
    val json: String,
    val timestamp: Long
)
