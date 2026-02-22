package com.example.cloudservices.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cloudservices.viewModel.WeatherViewModel

@Composable
fun WeatherForecastScreen(
    viewModel: WeatherViewModel
) {
    val forecast by viewModel.forecast

    LaunchedEffect(Unit) {
        // Oulu's coordinates
        viewModel.fetchDailyForecast(
            lat = 65.0121,
            lon = 25.4651
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF123A63))
            .padding(20.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Sääennuste",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(20.dp))

            if (forecast.isEmpty()) {
                CircularProgressIndicator(color = Color.White)
            } else {
                // The WeatherForecast composable does not exist yet.
                // This will be implemented in a future step.
                // WeatherForecast(forecast = forecast)
            }
        }
    }
}
