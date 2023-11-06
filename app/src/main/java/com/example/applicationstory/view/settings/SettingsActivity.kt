package com.example.applicationstory.view.settings

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.applicationstory.R
import com.example.applicationstory.data.pref.UserPreference
import com.example.applicationstory.data.pref.dataStore
import com.example.applicationstory.databinding.ActivitySettingsBinding
import com.example.applicationstory.view.ViewModelFactory
import com.example.applicationstory.view.settings.setting_theme.SettingPreferences
import com.example.applicationstory.view.settings.setting_theme.SettingThemeViewModel
import com.example.applicationstory.view.settings.setting_theme.ViewModelFactoryTheme
import com.example.applicationstory.view.settings.setting_theme.dataStoreSettings
import com.example.applicationstory.view.welcome.WelcomeActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(this, UserPreference.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupView()

        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)

        val pref = SettingPreferences.getInstance(application.dataStoreSettings)
        val settingViewModel = ViewModelProvider(this, ViewModelFactoryTheme(pref)).get(
            SettingThemeViewModel::class.java
        )
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }
        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }

        viewModel.logoutSuccess.observe(this) { success ->
            if (success) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
        playAnimation()
    }

    private fun setupAction() {
        binding.cardViewChangeLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.cardViewLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun setupView() {
        supportActionBar?.apply {
            title = getString(R.string.settings)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun playAnimation() {

        val changeLanguage = ObjectAnimator.ofFloat(binding.cardViewChangeLanguage, View.ALPHA, 1f).setDuration(200)
        val logout = ObjectAnimator.ofFloat(binding.cardViewLogout, View.ALPHA, 1f).setDuration(200)
        val switchTheme = ObjectAnimator.ofFloat(binding.switchTheme, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(changeLanguage, logout, switchTheme)
            start()
        }
    }
}