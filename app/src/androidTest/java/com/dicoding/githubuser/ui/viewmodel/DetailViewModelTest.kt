package com.dicoding.githubuser.ui.viewmodel

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.data.local.room.UserDatabase
import com.dicoding.githubuser.data.remote.retrofit.ApiConfig
import com.dicoding.githubuser.data.repository.UserRepository
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import junit.framework.TestCase

/*
Testing Scenario

Level: unit test
Feature: Add user to favorite
Test step: Real user add github user to their favorite
Expected result: Room database contain favorite user

 */

@RunWith(AndroidJUnit4::class)
class DetailViewModelTest: TestCase() {
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var db: UserDatabase

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val apiService = ApiConfig.getApiService()
        db = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java).build()
        val userDao = db.userDao()
        val userRepository = UserRepository.getInstance(apiService, userDao)
        detailViewModel = DetailViewModel(userRepository)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun setFavoriteUser() {
        val user = UserEntity("dewasemadi", null, null)
        detailViewModel.setFavoriteUser(user)

        val isFavorite = detailViewModel.isFavoriteUser(user.username)
        isFavorite.value?.let { assertTrue(it) }
    }
}