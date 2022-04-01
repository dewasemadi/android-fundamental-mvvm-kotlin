package com.dicoding.githubuser.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.data.repository.UserRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getFavoriteUsers() = userRepository.getFavoriteUsers()

    fun deleteFavoriteUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.deleteFavoriteUser(user)
        }
    }

    fun deleteAllFavorite() {
        viewModelScope.launch {
            userRepository.deleteAllFavorite()
        }
    }
}