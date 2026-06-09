package com.example.mdi2_109_weatherapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mdi2_109_weatherapp.databinding.ActivityMainBinding
import com.example.mdi2_109_weatherapp.network.AppConstants
import com.example.mdi2_109_weatherapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetWeather.setOnClickListener {
            val city = binding.etCity.text.toString().trim()
            if (city.isEmpty()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            fetchWeather(city)
        }
    }

    private fun fetchWeather(city: String) {
        // =======================================================
        // ASSIGNMENT 1 — Implement this function!
        // =======================================================
        // Steps to complete:
        // 1. Use lifecycleScope.launch { } to start a coroutine
        // 2. Inside it, use withContext(Dispatchers.IO) { } for the network call
        // 3. Call RetrofitClient.weatherApiService.getWeather(city, AppConstants.API_KEY, AppConstants.UNITS)
        // 4. Check if response.isSuccessful
        // 5. If YES: use response.body() to update binding.tvCity, binding.tvTemperature, binding.tvDescription
        // 6. If NO: show a Toast "City not found. Check the name and try again."
        // 7. Wrap everything in try { } catch (e: Exception) { } for network errors
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.weatherApiService.getWeather(
                        city,
                        AppConstants.API_KEY,
                        AppConstants.UNITS
                    )
                }

                if (response.isSuccessful) {
                    val weatherResponse = response.body()

                    weatherResponse?.let {
                        binding.tvCity.text = "City: ${it.name}"
                        binding.tvTemperature.text = "Temperature: ${it.main.temp}°C"
                        binding.tvDescription.text =
                            if(it.weather.isNotEmpty()) {
                                "Description: ${it.weather.firstOrNull()?.description}"
                            } else {
                                "No description"
                            }
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "City not found. Check the name and try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Network error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}