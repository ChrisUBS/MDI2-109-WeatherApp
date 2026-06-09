package com.example.mdi2_109_weatherapp.network

// "object" in Kotlin = a singleton class
// A singleton means there is exactly ONE instance in the whole app
// You never write: val c = AppConstants() — you just use: AppConstants.API_KEY
// Perfect for constants because you never want two different copies of your API key
object AppConstants {

    // Base URL = the root address of the API
    // Every call starts with this, then adds the specific endpoint path
    // IMPORTANT: must end with "/" — Retrofit requires this
    const val WEATHER_BASE_URL = "https://api.openweathermap.org/"

    // Replace "YOUR_API_KEY_HERE" with your actual key from openweathermap.org
    // My API Keys → copy the key → paste it here inside the quotes
    const val API_KEY = "YOUR_API_KEY_HERE"

    // "metric"  = Celsius degrees, wind in m/s
    // "imperial" = Fahrenheit, wind in mph
    // "standard" = Kelvin (default if omitted — 300K feels very hot!)
    const val UNITS = "metric"
}