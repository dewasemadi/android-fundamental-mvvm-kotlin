package com.dicoding.githubuser.utils

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

// wrapping a suspend function(in our example body lambda function) into a LiveData
inline fun <T> liveResponse(crossinline body: suspend () -> ResultResponse<T>) =
    liveData(Dispatchers.IO) {
        emit(ResultResponse.Loading)
        emit(body())
    }