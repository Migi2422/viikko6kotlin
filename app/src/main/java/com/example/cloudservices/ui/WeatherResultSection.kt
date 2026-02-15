package com.example.cloudservices.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherResultSection(
    city: String,
    temp: Double,
    feelsLike: Double,
    description: String,
    wind: Double,
    humidity: Int,
    pressure: Int,
    country: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "$city, $country",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0A3D62)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "${temp.toInt()}°C",
            fontSize = 56.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1B4F72)
        )

        Text(
            text = "Tuntuu kuin ${feelsLike.toInt()}°C",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFEAF2F8)
            ),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {

                Text(
                    text = description.replaceFirstChar { it.uppercase() },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A5276)
                )

                Spacer(Modifier.height(16.dp))

                WeatherInfoRow("Tuuli", "$wind m/s")
                WeatherInfoRow("Kosteus", "$humidity %")
                WeatherInfoRow("Paine", "$pressure hPa")
            }
        }
    }
}

@Composable
fun WeatherInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color(0xFF1B4F72)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1B4F72)
        )
    }
}
