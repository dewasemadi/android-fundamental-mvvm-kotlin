package com.dicoding.githubuser.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.api.ApiConfig
import com.dicoding.githubuser.model.SearchResponse
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.util.Event
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class MainViewModel : ViewModel() {
    private val _listUser = MutableLiveData<List<User>?>()
    val listUser: LiveData<List<User>?> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isNetworkError = MutableLiveData<Event<Boolean>>()
    val isNetworkError: LiveData<Event<Boolean>> = _isNetworkError

    fun getUsersByUsername(username: String) {
        _isLoading.value = true
        _isNetworkError.value = Event(false)
        val client = ApiConfig.getApiService().getUsersByUsername(username)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful)
                    _listUser.value = response.body()?.items
                else{
                    _isNetworkError.value = Event(true)
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                _isNetworkError.value = Event(true)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}