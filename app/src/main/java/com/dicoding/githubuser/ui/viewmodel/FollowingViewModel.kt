package com.dicoding.githubuser.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.repository.UserRepository
import com.dicoding.githubuser.utils.liveResponse

class FollowingViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUserFollowing(username: String) = liveResponse {
        userRepository.getUserFollowing(username)
    }
}