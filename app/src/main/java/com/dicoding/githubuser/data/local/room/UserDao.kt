package com.dicoding.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.githubuser.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)

    @Query("DELETE FROM user")
    suspend fun deleteAll()

    @Query("SELECT * FROM user ORDER BY username ASC")
    fun getFavoriteUsers(): LiveData<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM user WHERE username = :username)")
    fun isFavoriteUser(username: String): LiveData<Boolean>
}