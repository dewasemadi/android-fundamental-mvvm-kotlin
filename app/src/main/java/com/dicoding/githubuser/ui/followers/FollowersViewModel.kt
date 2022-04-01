package com.dicoding.githubuser.ui.followers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.api.ApiConfig
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {
    private val _listFollowers = MutableLiveData<List<User>?>()
    val listFollowers: LiveData<List<User>?> = _listFollowers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isNetworkError = MutableLiveData<Event<Boolean>>()
    val isNetworkError: LiveData<Event<Boolean>> = _isNetworkError

    fun getUserFollowers(username: String) {
        _isLoading.value = true
        _isNetworkError.value = Event(false)
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful)
                    _listFollowers.value = response.body()
                else{
                    _isNetworkError.value = Event(true)
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false
                _isNetworkError.value = Event(true)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "FollowersViewModel"
    }
}