package com.example.cloudservices.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cloudservices.data.model.ForecastListItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.draw.rotate
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.cloudservices.R


@Composable
fun ForecastSection(forecast: List<ForecastListItem>) {
    val dailyForecasts = forecast.filter { it.dt_txt.contains("12:00:00") }
    val WeatherBlue = Color(0xFF1B4F72)

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            "5 päivän sää",
            color = WeatherBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        dailyForecasts.forEach { item ->
            ForecastItem(item, WeatherBlue)
        }
    }
}

@Composable
fun ForecastItem(item: ForecastListItem, WeatherBlue: Color) {
    val date = Date(item.dt * 1000)
    val format = SimpleDateFormat("EEE, MMM d", Locale("fi"))

    val tempColor = when {
        item.main.temp < 0 -> Color(0xFF4FC3F7)      // kylmä
        item.main.temp < 10 -> Color(0xFF81D4FA)     // viileä
        item.main.temp < 20 -> Color(0xFFFFF176)     // lämmin
        else -> Color(0xFFFFA726)                    // kuuma
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF2F8)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Päivä
            Text(
                text = format.format(date),
                fontWeight = FontWeight.Bold,
                color = WeatherBlue
            )

            Spacer(Modifier.height(4.dp))

            // Sääikoni
            AsyncImage(
                model = "https://openweathermap.org/img/wn/${item.weather.first().icon}@2x.png",
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )

            // Lämpötila (säilytetään värikoodaus)
            Text(
                text = "Lämpötila: ${item.main.temp.toInt()}°C",
                color = tempColor,
                fontWeight = FontWeight.SemiBold
            )

            // Tuuli + suunta
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Tuuli: ${item.wind.speed} m/s",
                    color = WeatherBlue
                )
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_up),
                    contentDescription = "tuulen suunta",
                    modifier = Modifier.rotate(item.wind.deg.toFloat()),
                    tint = WeatherBlue
                )
            }

            // Pilvisyys
            Text("Pilvisyys: ${item.clouds.all}%", color = WeatherBlue)

            // Sade
            item.rain?.`3h`?.let {
                Text("Sade: ${it} mm", color = WeatherBlue)
            } ?: Text("Ei sadetta", color = WeatherBlue)

            // Lumi
            item.snow?.`3h`?.let {
                Text("Lumen määrä: ${it} mm", color = WeatherBlue)
            } ?: Text("Ei lumisadetta", color = WeatherBlue)
        }
    }
}


