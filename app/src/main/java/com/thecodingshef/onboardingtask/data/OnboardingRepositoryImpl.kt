package com.thecodingshef.onboardingtask.data

import com.thecodingshef.onboardingtask.core.base.BaseRepository
import com.thecodingshef.onboardingtask.data.models.OnboardingResponse
import com.thecodingshef.onboardingtask.data.remote.ApiService
import com.thecodingshef.onboardingtask.domain.OnboardingRepository
import com.thecodingshef.onboardingtask.core.base.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnboardingRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : OnboardingRepository, BaseRepository() {

//    override suspend fun getOnboardingData(): Flow<Resource<OnboardingResponse>> = flow {
//        try {
//            emit(Resource.Loading())
//            val response = apiService.getOnboardingData()
//            if (response.isSuccessful && response.body() != null) {
//                emit(Resource.Success(response.body()!!))
//            } else {
//                emit(Resource.Error("Failed to fetch data"))
//            }
//        } catch (e: Exception) {
//            emit(Resource.Error(e.message ?: "Unknown error occurred"))
//        }
//    }
//

    override suspend fun getOnboardingData(): Flow<Resource<OnboardingResponse>> {
        return safeApiCall {
            apiService.getOnboardingData()
        }
    }

}