package com.thecodingshef.onboardingtask.domain

import com.thecodingshef.onboardingtask.data.models.OnboardingResponse
import com.thecodingshef.onboardingtask.core.base.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOnboardingDataUseCase @Inject constructor(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(): Flow<Resource<OnboardingResponse>> {
        return repository.getOnboardingData()
    }
}