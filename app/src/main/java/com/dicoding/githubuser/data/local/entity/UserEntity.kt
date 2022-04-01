package com.dicoding.githubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    // github username is unique so it can be a primary key
    @field: ColumnInfo(name = "username")
    @field: PrimaryKey
    val username: String,

    @field:ColumnInfo(name = "avatar_url")
    val avatarUrl: String?,

    @field: ColumnInfo(name = "name")
    val name: String?,
)