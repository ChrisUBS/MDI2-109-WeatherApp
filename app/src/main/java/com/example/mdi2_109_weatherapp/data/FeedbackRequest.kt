package com.example.mdi2_109_weatherapp.data

// This data class represents the JSON body we SEND to APIdog
// Gson converts this Kotlin object into JSON

data class FeedbackRequest(
    val city: String,
    val rating: Int,
    val comment: String
)