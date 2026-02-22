package com.example.cloudservices.data.repository

import com.example.cloudservices.data.local.WeatherDao
import com.example.cloudservices.data.model.ForecastResponse
import com.example.cloudservices.data.model.WeatherEntity
import com.example.cloudservices.data.model.WeatherResponse
import com.example.cloudservices.data.remote.RetrofitInstance
import com.example.cloudservices.data.remote.WeatherApi

class WeatherRepository(
    private val api: WeatherApi,
    private val dao: WeatherDao
) {

    suspend fun getWeather(city: String): WeatherResponse {
        val cached = dao.getWeather(city)

        if (cached != null && System.currentTimeMillis() - cached.timestamp < 30 * 60 * 1000) {
            return RetrofitInstance.gson.fromJson(cached.json, WeatherResponse::class.java)
        }

        val fresh = api.getWeatherByCity(city)

        dao.insertWeather(
            WeatherEntity(
                city = city,
                json = RetrofitInstance.gson.toJson(fresh),
                timestamp = System.currentTimeMillis()
            )
        )

        return fresh
    }

    suspend fun getDailyForecast(lat: Double, lon: Double): ForecastResponse {
        return api.getDailyForecast(lat, lon)
    }

    suspend fun getWeatherByCoordinates(lat: Double, lon: Double): WeatherResponse {
        return api.getWeatherByCoordinates(lat, lon)
    }
}
