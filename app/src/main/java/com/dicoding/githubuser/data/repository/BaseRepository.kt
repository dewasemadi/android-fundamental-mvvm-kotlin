package com.dicoding.githubuser.data.repository

import com.dicoding.githubuser.utils.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRepository {
    suspend inline fun <T> safeApiCall(crossinline body: suspend () -> T): ResultResponse<T> {
        return try {
            // blocking block
            val response = withContext(Dispatchers.IO) {
                body()
            }
            ResultResponse.Success(response)
        } catch (e: Exception) {
            ResultResponse.Error(e.message.toString())
        }
    }
}