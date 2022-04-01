package com.dicoding.githubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.githubuser.data.repository.SettingsRepository
import com.dicoding.githubuser.data.repository.UserRepository
import com.dicoding.githubuser.utils.liveResponse

class MainViewModel(private val userRepository: UserRepository, private val settingsRepository: SettingsRepository) : ViewModel() {
    fun getUsersByUsername(query: String) = liveResponse {
        userRepository.getUsersByUsername(query)
    }

    fun getThemeSettings(): LiveData<Boolean> = settingsRepository.getThemeSetting().asLiveData()
}
