package com.example.mdi2_109_weatherapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mdi2_109_weatherapp.data.FeedbackRequest
import com.example.mdi2_109_weatherapp.databinding.ActivityMainBinding
import com.example.mdi2_109_weatherapp.network.AppConstants
import com.example.mdi2_109_weatherapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentCity: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClient.init(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetWeather.setOnClickListener {
            val city = binding.etCity.text.toString().trim()
            if (city.isEmpty()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            currentCity = city // save the city before fetching weather
            fetchWeather(city)
        }

        binding.btnSubmitFeedback.setOnClickListener {
            if (currentCity.isEmpty()) {
                Toast.makeText(this, "Please get weather first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val rating = binding.ratingBar.rating.toInt()
            val comment = binding.etComment.text.toString().trim()
            if (comment.isEmpty()) {
                Toast.makeText(this, "Please enter a comment!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            submitFeedback(currentCity, rating, comment)
        }
    }

    private fun fetchWeather(city: String) {
        lifecycleScope.launch {
            try {
                binding.progressBar.visibility = View.VISIBLE
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
                            if (it.weather.isNotEmpty()) {
                                "Description: ${it.weather.firstOrNull()?.description}"
                            } else {
                                "No description"
                            }
                        binding.tvWindSpeed.text = "Wind Speed: ${it.wind.speed} m/s"
                        binding.tvHumidity.text = "Humidity: ${it.main.humidity}%"
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
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun submitFeedback(city: String, rating: Int, comment: String) {
        val request = FeedbackRequest(city, rating, comment)

        lifecycleScope.launch {
            try {
                binding.progressBarFeedback.visibility = View.VISIBLE
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.feedbackApiService.submitFeedback(request)
                }

                if (response.isSuccessful) {
                    val feedbackResponse = response.body()
                    val message = feedbackResponse?.message ?: "Feedback submitted successfully!"
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                    // binding.tvFeedbackResult.text = message

                    // Clear fields after success
                    binding.ratingBar.rating = 0f
                    binding.etComment.text.clear()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to submit feedback. Error code: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Network error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                binding.progressBarFeedback.visibility = View.GONE
            }
        }
    }
}