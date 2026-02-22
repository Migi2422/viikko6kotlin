package com.example.cloudservices.data.local

import android.content.Context
import androidx.room.Room
import com.example.cloudservices.data.remote.RetrofitInstance
import com.example.cloudservices.data.repository.WeatherRepository

object AppModule {

    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "weather.db"
        ).build()
    }

    fun provideWeatherDao(db: AppDatabase): WeatherDao {
        return db.weatherDao()
    }

    fun provideWeatherRepository(db: AppDatabase): WeatherRepository {
        return WeatherRepository(RetrofitInstance.api, db.weatherDao())
    }
}
