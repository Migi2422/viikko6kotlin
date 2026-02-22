package com.example.cloudservices.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloudservices.data.model.ForecastListItem
import com.example.cloudservices.data.model.WeatherResponse
import com.example.cloudservices.data.repository.WeatherRepository
import kotlinx.coroutines.launch

data class WeatherUiState(
    val city: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val weather: WeatherResponse? = null
)

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    var uiState = mutableStateOf(WeatherUiState())
        private set

    private val _forecast = mutableStateOf<List<ForecastListItem>>(emptyList())
    val forecast: State<List<ForecastListItem>> = _forecast

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
                val response = repository.getWeather(city)

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
                val response = repository.getDailyForecast(lat, lon)
                _forecast.value = response.list
            } catch (e: Exception) {
                println("Forecast error: ${e.message}")
            }
        }
    }

    fun fetchWeatherForMap(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = repository.getWeatherByCoordinates(lat, lon)
                _mapWeather.value = response
            } catch (e: Exception) {
                println("Map weather error: ${e.message}")
            }
        }
    }
}
