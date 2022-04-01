package com.dicoding.githubuser.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.githubuser.R
import com.dicoding.githubuser.ui.viewmodel.SettingsViewModel
import com.dicoding.githubuser.ui.viewmodel.ViewModelFactory
import com.dicoding.githubuser.utils.DELAY

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            val toMain = Intent(this, MainActivity::class.java)
            startActivity(toMain)
            finish()
        }, DELAY)

         observeThemeSetting()
    }

    private fun observeThemeSetting(){
        settingsViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            val default = if(isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(default)
        }
    }
}