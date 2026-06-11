package com.example.mdi2_109_weatherapp.network

import com.example.mdi2_109_weatherapp.data.FeedbackRequest
import com.example.mdi2_109_weatherapp.data.FeedbackResponse
import retrofit2.Response
import retrofit2.http.Body // @Body = sends this object as the JSON request body
import retrofit2.http.POST // @POST = HTTP POST method

interface FeedbackApiService {

    @POST(value = "feedback")
    suspend fun submitFeedback(
        @Body request: FeedbackRequest
    ): Response<FeedbackResponse>
}