package com.dicoding.githubuser.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.repository.UserRepository
import com.dicoding.githubuser.utils.liveResponse

class FollowersViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUserFollowers(username: String) = liveResponse {
        userRepository.getUserFollowers(username)
    }
}