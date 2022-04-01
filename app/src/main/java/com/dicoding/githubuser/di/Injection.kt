package com.dicoding.githubuser.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dicoding.githubuser.data.local.datastore.SettingPreferences
import com.dicoding.githubuser.data.repository.UserRepository
import com.dicoding.githubuser.data.local.room.UserDatabase
import com.dicoding.githubuser.data.remote.retrofit.ApiConfig
import com.dicoding.githubuser.data.repository.SettingsRepository

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        return UserRepository.getInstance(apiService, dao)
    }

    fun provideSettingsRepository(dataStore: DataStore<Preferences>): SettingsRepository{
        val settingPreferences = SettingPreferences.getInstance(dataStore)
        return SettingsRepository.getInstance(settingPreferences)
    }
}