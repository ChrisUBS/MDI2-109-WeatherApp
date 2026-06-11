package com.example.mdi2_109_weatherapp.data

// This data class represents what APIdog sends back after we POST
// APIdog returns minimal data to know if it succeeded

data class FeedbackResponse(
    val message: String? = null, // optional success message from APIdog
    val success: Boolean? = null // optional success flag
)