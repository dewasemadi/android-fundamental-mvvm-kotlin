package com.dicoding.githubuser.data.repository

import com.dicoding.githubuser.data.local.datastore.SettingPreferences
import kotlinx.coroutines.flow.Flow

class SettingsRepository private constructor(private val settingPreferences: SettingPreferences) {

    fun getThemeSetting(): Flow<Boolean> = settingPreferences.getThemeSetting()

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        settingPreferences.saveThemeSetting(isDarkModeActive)
    }

    companion object {
        @Volatile
        private var instance: SettingsRepository? = null
        fun getInstance(
            settingPreferences: SettingPreferences
        ): SettingsRepository =
            instance ?: synchronized(this) {
                instance ?: SettingsRepository(settingPreferences)
            }.also { instance = it }
    }
}