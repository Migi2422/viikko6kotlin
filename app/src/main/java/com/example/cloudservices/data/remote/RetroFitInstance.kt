package com.example.cloudservices.data.remote

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val gson: Gson by lazy { Gson() }

    // Create a custom OkHttpClient to add the AuthInterceptor
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .client(okHttpClient) // Use the custom client
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val api: WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }
}
