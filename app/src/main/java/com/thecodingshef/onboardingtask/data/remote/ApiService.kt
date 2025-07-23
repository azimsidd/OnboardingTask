package com.thecodingshef.onboardingtask.data.remote

import com.thecodingshef.onboardingtask.data.models.OnboardingResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/")
    suspend fun getOnboardingData(): Response<OnboardingResponse>
}