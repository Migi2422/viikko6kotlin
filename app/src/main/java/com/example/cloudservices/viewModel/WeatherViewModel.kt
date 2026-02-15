package com.example.cloudservices.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloudservices.BuildConfig
import com.example.cloudservices.data.model.*
import com.example.cloudservices.data.remote.RetrofitInstance
import kotlinx.coroutines.launch

data class WeatherUiState(
    val city: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val weather: WeatherResponse? = null
)

class WeatherViewModel : ViewModel() {

    var uiState = mutableStateOf(WeatherUiState())
        private set

    private val _forecast = mutableStateOf<List<ForecastListItem>>(emptyList())
    val forecast: State<List<ForecastListItem>> = _forecast

    // New state for map-specific weather data
    private val _mapWeather = mutableStateOf<WeatherResponse?>(null)
    val mapWeather: State<WeatherResponse?> = _mapWeather

    fun onCityChange(newCity: String) {
        uiState.value = uiState.value.copy(city = newCity)
    }

    fun fetchWeather() {
        val city = uiState.value.city.trim()
        if (city.isEmpty()) {
            uiState.value = uiState.value.copy(error = "Syötä kaupunki")
            return
        }

        uiState.value = uiState.value.copy(
            isLoading = true,
            error = null,
            weather = null
        )

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getWeatherByCity(
                    city = city,
                    apiKey = BuildConfig.OPEN_WEATHER_API_KEY
                )

                uiState.value = uiState.value.copy(
                    isLoading = false,
                    weather = response
                )

                fetchDailyForecast(
                    lat = response.coord.lat,
                    lon = response.coord.lon
                )

            } catch (e: Exception) {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    error = "Sään haku epäonnistui: ${e.message}"
                )
            }
        }
    }

    fun fetchDailyForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getDailyForecast(
                    lat = lat,
                    lon = lon,
                    apiKey = BuildConfig.OPEN_WEATHER_API_KEY
                )
                _forecast.value = response.list
            } catch (e: Exception) {
                // Print error to logcat for debugging
                println("Forecast error: ${e.message}")
            }
        }
    }

    // New function to fetch weather for the map
    fun fetchWeatherForMap(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getWeatherByCoordinates(
                    lat = lat,
                    lon = lon,
                    apiKey = BuildConfig.OPEN_WEATHER_API_KEY
                )
                _mapWeather.value = response
            } catch (e: Exception) {
                println("Map weather error: ${e.message}")
            }
        }
    }
}
