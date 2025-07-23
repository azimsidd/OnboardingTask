package com.thecodingshef.onboardingtask.core.base

import com.thecodingshef.onboardingtask.core.base.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepository {

    protected fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Flow<Resource<T>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiCall()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    emit(Resource.Success(body))
                } else {
                    emit(Resource.Error("Empty response body"))
                }
            } else {

                val errorMsg = parseHttpError(response)
                emit(Resource.Error(errorMsg))
            }

        } catch (e: IOException) {
            emit(Resource.Error("Network error. Please check your connection."))
        } catch (e: HttpException) {
            emit(Resource.Error("HTTP error: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.message ?: "Something went wrong"}"))
        }
    }

    private fun <T> parseHttpError(response: Response<T>): String {
        return when (response.code()) {
            400 -> "Bad request. Please try again."
            401 -> "Unauthorized. Please login again."
            403 -> "Access denied."
            404 -> "Resource not found."
            422 -> "Validation failed. Check your input."
            500 -> "Server error. Try again later."
            else -> {
                val rawError = response.errorBody()?.string()
                rawError ?: "Unexpected error: ${response.code()}"
            }
        }
    }

}