package com.dicoding.githubuser.data.remote.retrofit

import com.dicoding.githubuser.data.remote.response.SearchResponse
import com.dicoding.githubuser.data.remote.response.User
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    suspend fun getUsersByUsername(@Query("q") query: String): SearchResponse

    @GET("users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): User

    @GET("users/{username}/followers")
    suspend fun getUserFollowers(@Path("username") username: String): List<User>

    @GET("users/{username}/following")
    suspend fun getUserFollowing(@Path("username") username: String): List<User>
}