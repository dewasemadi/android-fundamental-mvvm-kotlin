package com.dicoding.githubuser.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivitySettingsBinding
import com.dicoding.githubuser.ui.viewmodel.SettingsViewModel
import com.dicoding.githubuser.ui.viewmodel.ViewModelFactory

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    private val settingsViewModel: SettingsViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        initToolbar()
        initSwitchTheme()
    }

    private fun initViewBinding() {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initToolbar() {
        supportActionBar?.apply {
            this.title = getString(R.string.settings)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initSwitchTheme() {
        binding.apply {
            settingsViewModel.getThemeSettings().observe(this@SettingsActivity
            ) { isDarkModeActive: Boolean ->
                switchTheme.isChecked = isDarkModeActive
                val default = if(isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                AppCompatDelegate.setDefaultNightMode(default)
            }

            switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                settingsViewModel.saveThemeSetting(isChecked)
            }
        }
    }
}