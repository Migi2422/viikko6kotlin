package com.example.cloudservices.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cloudservices.viewModel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    navController: NavController,
    viewModel: WeatherViewModel = viewModel()
) {
    val state by viewModel.uiState
    val forecast by viewModel.forecast
    val scrollState = rememberScrollState()
    
    // A single Column now holds all content and is scrollable
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFd6eaf8))
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SääMappi",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = state.city,
                onValueChange = { viewModel.onCityChange(it) },
                label = { Text("Kaupunki") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1B4F72),
                    unfocusedBorderColor = Color(0xFF5DADE2),
                    focusedLabelColor = Color(0xFF1B4F72),
                    focusedTextColor = Color.Black,
                    cursorColor = Color(0xFF1B4F72)
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.fetchWeather() },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1B4F72)
            )
        ) {
            Text(
                "Hae sää",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (state.isLoading) {
            CircularProgressIndicator(color = Color(0xFF1B4F72))
        }

        state.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }

        state.weather?.let { weather ->
            WeatherResultSection(
                city = weather.name,
                temp = weather.main.temp,
                feelsLike = weather.main.feels_like,
                description = weather.weather.firstOrNull()?.description ?: "",
                wind = weather.wind.speed,
                humidity = weather.main.humidity,
                pressure = weather.main.pressure,
                country = weather.sys.country
            )

            SunCard(
                sunrise = weather.sys.sunrise,
                sunset = weather.sys.sunset
            )

            Button(
                onClick = { navController.navigate("map") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1B4F72)
                )
            ) {
                Text("Avaa kartta", color = Color.White)
            }

            if (forecast.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                ForecastSection(forecast = forecast)
            }
        }
    }
}

@Composable
fun SunCard(sunrise: Long, sunset: Long) {
    val format = SimpleDateFormat("HH:mm", Locale("fi"))

    val sunriseTime = format.format(Date(sunrise * 1000))
    val sunsetTime = format.format(Date(sunset * 1000))

    val WeatherBlue = Color(0xFF1B4F72)

    Card(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .width(350.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF2F8)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Aurinko",
                fontWeight = FontWeight.Bold,
                color = WeatherBlue
            )

            Spacer(Modifier.height(4.dp))

            Text(
                "Auringonnousu: $sunriseTime",
                color = WeatherBlue
            )
            Text(
                "Auringonlasku: $sunsetTime",
                color = WeatherBlue
            )
        }
    }
}
