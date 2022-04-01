package com.dicoding.githubuser.data.repository

import androidx.lifecycle.LiveData
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.data.local.room.UserDao
import com.dicoding.githubuser.data.remote.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService,
    private val usersDao: UserDao,
) : BaseRepository() {
    // remote data source
    suspend fun getUsersByUsername(query: String) = safeApiCall {
        apiService.getUsersByUsername(query)
    }

    suspend fun getUserDetail(username: String) = safeApiCall {
        apiService.getUserDetail(username)
    }

    suspend fun getUserFollowers(username: String) = safeApiCall {
        apiService.getUserFollowers(username)
    }

    suspend fun getUserFollowing(username: String) = safeApiCall {
        apiService.getUserFollowing(username)
    }

    // local data source
    suspend fun addToFavoriteUser(user: UserEntity) = usersDao.insert(user)

    suspend fun deleteFavoriteUser(user: UserEntity) = usersDao.delete(user)

    suspend fun deleteAllFavorite() = usersDao.deleteAll()

    fun getFavoriteUsers(): LiveData<List<UserEntity>> = usersDao.getFavoriteUsers()

    fun isFavoriteUser(username: String): LiveData<Boolean> = usersDao.isFavoriteUser(username)

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            usersDao: UserDao,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, usersDao)
            }.also { instance = it }
    }
}