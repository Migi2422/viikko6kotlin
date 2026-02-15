package com.example.cloudservices.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.TilesOverlay
import com.example.cloudservices.BuildConfig
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudservices.R
import org.osmdroid.util.MapTileIndex
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cloudservices.viewModel.WeatherViewModel
import org.osmdroid.views.overlay.Marker

private fun createWeatherTileSource(layer: String): XYTileSource {
    val apiKey = BuildConfig.OPEN_WEATHER_API_KEY
    return object : XYTileSource(
        "OpenWeather-$layer", 0, 18, 256, ".png", arrayOf("https://ignored.url/")
    ) {
        override fun getTileURLString(pMapTileIndex: Long): String {
            return "https://tile.openweathermap.org/map/$layer/" +
                    MapTileIndex.getZoom(pMapTileIndex) + "/" +
                    MapTileIndex.getX(pMapTileIndex) + "/" +
                    MapTileIndex.getY(pMapTileIndex) + mImageFilenameEnding +
                    "?appid=$apiKey"
        }
    }
}

@Composable
fun WeatherLayerSelector(
    modifier: Modifier = Modifier,
    currentLayer: String,
    onLayerSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val layers = listOf(
        "Pilvet" to "clouds_new",
        "Sade" to "precipitation_new",
        "Lämpötila" to "temp_new"
    )

    Box(modifier = modifier) {
        Button(onClick = { expanded = true }) {
            Text("valitse kartta: ${layers.firstOrNull { it.second == currentLayer }?.first ?: currentLayer}")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            layers.forEach { (name, key) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        expanded = false
                        onLayerSelected(key)
                    }
                )
            }
        }
    }
}

@Composable
fun TemperatureLegend(modifier: Modifier = Modifier) {
    // This legend provides a more detailed and accurate color scale for temperatures.
    val legendData = listOf(
        Color(0xFFB71C1C) to "> 40°C",
        Color(0xFFD32F2F) to "30-40°C",
        Color(0xFFF57C00) to "20-30°C",
        Color(0xFFFFEB3B) to "10-20°C",
        Color(0xFF7CB342) to "0-10°C",
        Color(0xFF4FC3F7) to "-10-0°C",
        Color(0xFF1976D2) to "-20 to -10°C",
        Color(0xFF303F9F) to "-30 to -20°C",
        Color(0xFF673AB7) to "-40 to -30°C",
        Color(0xFFE3F2FD) to "< -40°C"
    )

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Lämpötilakartta (°C)", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            legendData.forEach { (color, text) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(16.dp).background(color))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun RainLegend(modifier: Modifier = Modifier) {
    val legendData = listOf(
        Color(0x80B3E5FC) to "Tihku / Ei sadetta",
        Color(0xFF4FC3F7) to "Kevyt sade (~0.1-1 mm/h)",
        Color(0xFF0288D1) to "Kohtalainen sade (~1-10 mm/h)",
        Color(0xFF311B92) to "Kova sade (~10-140 mm/h)",
        Color(0xFF6A1B9A) to "Äärimmäinen sade (>140 mm/h)"
    )

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Sateen intensiteetti", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            legendData.forEach { (color, text) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(16.dp).background(color))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun MapScreen(
    viewModel: WeatherViewModel = viewModel()
) {
    var selectedLayer by remember { mutableStateOf("clouds_new") }
    val mapWeather by viewModel.mapWeather
    val centerPoint = GeoPoint(65.0121, 25.4651)

    val tileSources = remember {
        listOf("clouds_new", "precipitation_new", "temp_new")
            .associateWith { layer -> createWeatherTileSource(layer) }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchWeatherForMap(centerPoint.latitude, centerPoint.longitude)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                MapView(context).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    controller.setZoom(5.0)
                    controller.setCenter(centerPoint)

                    val provider = MapTileProviderBasic(context)
                    val overlay = TilesOverlay(provider, context)
                    overlays.add(overlay)

                    val marker = Marker(this).apply {
                        position = centerPoint
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        icon = context.getDrawable(org.osmdroid.library.R.drawable.person)
                        setTag(R.id.map_marker, this)
                    }
                    overlays.add(marker)

                    setTag(R.id.map_tile_provider, provider)
                }
            },
            update = { mapView ->
                val provider = mapView.getTag(R.id.map_tile_provider) as MapTileProviderBasic

                provider.setTileSource(tileSources[selectedLayer])
                provider.clearTileCache()

                val marker = mapView.getTag(R.id.map_marker) as Marker
                mapWeather?.let {
                    marker.title = "Säätiedot"
                    val tempInfo = "Lämpötila: ${it.main.temp.toInt()}°C"
                    val rainObject = it.rain
                    val rainValue = rainObject?.`1h`
                    val rainAmount = rainValue ?: 0.0
                    val rainInfo = "Sade (1h): $rainAmount mm"
                    val windInfo = "Tuuli: ${it.wind.speed} m/s"
                    marker.snippet = """
                        $tempInfo
                        $windInfo
                        $rainInfo
                    """.trimIndent()
                    marker.showInfoWindow()
                }

                mapView.invalidate()
            }
        )

        WeatherLayerSelector(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 56.dp, start = 16.dp),
            currentLayer = selectedLayer,
            onLayerSelected = { selectedLayer = it }
        )

        if (selectedLayer == "temp_new") {
            TemperatureLegend(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 100.dp)
            )
        }

        if (selectedLayer == "precipitation_new") {
            RainLegend(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 100.dp)
            )
        }
    }
}
