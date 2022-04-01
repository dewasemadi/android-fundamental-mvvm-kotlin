package com.dicoding.githubuser.ui.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubuser.data.repository.SettingsRepository
import com.dicoding.githubuser.data.repository.UserRepository
import com.dicoding.githubuser.di.Injection

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(userRepository, settingsRepository) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(userRepository) as T
            modelClass.isAssignableFrom(FollowersViewModel::class.java) -> FollowersViewModel(userRepository) as T
            modelClass.isAssignableFrom(FollowingViewModel::class.java) -> FollowingViewModel(userRepository) as T
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> FavoritesViewModel(userRepository) as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(settingsRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideUserRepository(context),
                    Injection.provideSettingsRepository(context.dataStore)
                )
            }.also { instance = it }
    }
}