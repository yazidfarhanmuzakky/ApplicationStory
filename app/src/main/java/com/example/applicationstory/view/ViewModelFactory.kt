package com.example.applicationstory.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.applicationstory.data.UserRepository
import com.example.applicationstory.data.local.StoryRepository
import com.example.applicationstory.data.pref.UserPreference
import com.example.applicationstory.data.retrofit.ApiService
import com.example.applicationstory.di.Injection
import com.example.applicationstory.view.maps.MapsViewModel
import com.example.applicationstory.view.login.LoginViewModel
import com.example.applicationstory.view.main.MainViewModel
import com.example.applicationstory.view.settings.SettingsViewModel
import com.example.applicationstory.view.signup.SignupViewModel

class ViewModelFactory(
    private val repository: UserRepository,
    private val apiService: ApiService,
    private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(storyRepository, repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository, apiService) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(apiService) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context, userPreference: UserPreference): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideRepository(context),
                        Injection.provideApiService(),
                        Injection.provideStoryRepository(context, userPreference)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}