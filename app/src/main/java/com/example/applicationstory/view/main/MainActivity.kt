package com.example.applicationstory.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.applicationstory.R
import com.example.applicationstory.adapter.LoadingStateAdapter
import com.example.applicationstory.adapter.QuoteListAdapter
import com.example.applicationstory.data.pref.UserPreference
import com.example.applicationstory.data.pref.dataStore
import com.example.applicationstory.data.response.QuoteResponseItem
import com.example.applicationstory.databinding.ActivityMainBinding
import com.example.applicationstory.view.ViewModelFactory
import com.example.applicationstory.view.maps.MapsActivity
import com.example.applicationstory.view.addStory.AddStoryActivity
import com.example.applicationstory.view.detail.DetailStoryActivity
import com.example.applicationstory.view.settings.SettingsActivity
import com.example.applicationstory.view.settings.setting_theme.SettingPreferences
import com.example.applicationstory.view.settings.setting_theme.SettingThemeViewModel
import com.example.applicationstory.view.settings.setting_theme.ViewModelFactoryTheme
import com.example.applicationstory.view.settings.setting_theme.dataStoreSettings
import com.example.applicationstory.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this, UserPreference.getInstance(dataStore))
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(true)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                    getData()
                    showLoading(false)
            }
        }

//        viewModel.getListStories().observe(this, {
//            if (it != null) {
//                adapter.setList(it)
//            } else {
//                AlertDialog.Builder(this@MainActivity).apply {
//                    setTitle(R.string.gagal_memuat_data)
//                    setMessage(R.string.gagal_memuat_data)
//                    setPositiveButton(R.string.oke) { _, _ ->
//                    }
//                    val intent = Intent(context, MainActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                    startActivity(intent)
//                    finish()
//                    showLoading(false)
//                }
//            }
//        })

        binding.imageAdd.setOnClickListener {
                lifecycleScope.launch {
                    val token = UserPreference.getInstance(dataStore).getToken()
                    Intent(this@MainActivity, AddStoryActivity::class.java).also {
                        it.putExtra(AddStoryActivity.EXTRA_TOKEN, token)
                        startActivity(it)
                    }
                }
        }

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
        }

        val pref = SettingPreferences.getInstance(application.dataStoreSettings)
        val settingViewModel = ViewModelProvider(this, ViewModelFactoryTheme(pref)).get(
            SettingThemeViewModel::class.java
        )
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        playAnimation()
    }

    private fun getData() {
        val adapter = QuoteListAdapter()
        adapter.setOnItemClickCallback(object : QuoteListAdapter.OnItemClickcallBack {
            override fun onItemClicked(data: QuoteResponseItem) {
                lifecycleScope.launch {
                    val token = UserPreference.getInstance(dataStore).getToken()
                    Intent(this@MainActivity, DetailStoryActivity::class.java).also {
                        it.putExtra(DetailStoryActivity.EXTRA_ID, data.id)
                        it.putExtra(DetailStoryActivity.EXTRA_TOKEN, token)
                        startActivity(it)
                    }
                }
            }
        })
        binding.rvUser.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.story.observe(this, {
            adapter.submitData(lifecycle, it)
        })
        showLoading(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {

        val user = ObjectAnimator.ofFloat(binding.rvUser, View.ALPHA, 1f).setDuration(200)
        val addButton = ObjectAnimator.ofFloat(binding.imageAdd, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(user, addButton)
            start()
        }
    }

}