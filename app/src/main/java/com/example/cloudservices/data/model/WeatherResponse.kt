package com.example.cloudservices.data.model

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h") val `1h`: Double?
)

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val sys: Sys,
    val coord: Coord,
    val rain: Rain?
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class Weather(
    val description: String
)

data class Wind(
    val speed: Double,
    val deg: Int
)

data class Sys(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

data class Coord(
    val lon: Double,
    val lat: Double
)
