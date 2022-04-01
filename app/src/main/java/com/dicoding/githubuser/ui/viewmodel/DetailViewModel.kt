package com.dicoding.githubuser.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.data.repository.UserRepository
import com.dicoding.githubuser.utils.liveResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUserDetail(username: String) = liveResponse {
        userRepository.getUserDetail(username)
    }

    fun setFavoriteUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.addToFavoriteUser(user)
        }
    }

    fun deleteFavoriteUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.deleteFavoriteUser(user)
        }
    }

    fun isFavoriteUser(username: String) = userRepository.isFavoriteUser(username)
}