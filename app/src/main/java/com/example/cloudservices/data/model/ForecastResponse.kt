package com.example.cloudservices.data.model

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    val list: List<ForecastListItem>
)

data class ForecastListItem(
    val dt: Long,
    val main: ForecastMain,
    val weather: List<ForecastWeather>,
    val wind: ForecastWind,
    val clouds: ForecastClouds,
    val dt_txt: String,
    val rain: ForecastRain?,
    val snow: ForecastSnow?
)

data class ForecastMain(
    val temp: Double
)

data class ForecastWeather(
    val description: String,
    val icon: String
)

data class ForecastWind(
    val speed: Double,
    val deg: Int
)

data class ForecastClouds(
    val all: Int
)

data class ForecastRain(
    @SerializedName("3h") val `3h`: Double?
)

data class ForecastSnow(
    @SerializedName("3h") val `3h`: Double?
)
