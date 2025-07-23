package com.thecodingshef.onboardingtask.domain

import com.thecodingshef.onboardingtask.data.models.OnboardingResponse
import com.thecodingshef.onboardingtask.core.base.Resource
import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    suspend fun getOnboardingData(): Flow<Resource<OnboardingResponse>>
}
