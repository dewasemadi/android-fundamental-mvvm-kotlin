package com.dicoding.githubuser.data.remote.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @field:SerializedName("items")
    val items: List<User>?
)

data class User(
    @field:SerializedName("login")
    val username: String?,

    @field:SerializedName("avatar_url")
    val avatarUrl: String?,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("company")
    val company: String?,

    @field:SerializedName("blog")
    val blog: String?,

    @field:SerializedName("location")
    val location: String?,

    @field:SerializedName("email")
    val email: String?,

    @field:SerializedName("bio")
    val bio: String?,

    @field:SerializedName("twitter_username")
    val twitterUsername: String?,

    @field:SerializedName("public_repos")
    val publicRepos: Int?,

    @field:SerializedName("public_gists")
    val publicGists: Int?,

    @field:SerializedName("followers")
    val followers: Int?,

    @field:SerializedName("following")
    val following: Int?,
)
