package com.example.cloudservices

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.cloudservices.data.local.AppDatabase
import com.example.cloudservices.data.remote.RetrofitInstance
import com.example.cloudservices.data.repository.WeatherRepository
import com.example.cloudservices.ui.MapScreen
import com.example.cloudservices.ui.WeatherForecastScreen
import com.example.cloudservices.ui.WeatherScreen
import com.example.cloudservices.ui.theme.CloudServicesTheme
import com.example.cloudservices.viewModel.WeatherViewModel
import com.example.cloudservices.viewModel.WeatherViewModelFactory

class MainActivity : ComponentActivity() {

    // Create the database and repository once, and pass them to the factory
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "weather-db"
        ).build()
    }

    private val repository by lazy { WeatherRepository(RetrofitInstance.api, database.weatherDao()) }

    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CloudServicesTheme {
                AppNavigation(viewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: WeatherViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "weather") {
        composable("weather") {
            WeatherScreen(navController = navController, viewModel = viewModel)
        }
        composable("forecast") {
            WeatherForecastScreen(viewModel = viewModel)
        }
        composable("map") {
            MapScreen(viewModel = viewModel)
        }
    }
}
