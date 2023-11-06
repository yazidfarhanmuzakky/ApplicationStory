package com.example.applicationstory.di

import android.content.Context
import com.example.applicationstory.data.UserRepository
import com.example.applicationstory.data.database.StoryDatabase
import com.example.applicationstory.data.local.StoryRepository
import com.example.applicationstory.data.pref.UserPreference
import com.example.applicationstory.data.pref.dataStore
import com.example.applicationstory.data.retrofit.ApiConfig
import com.example.applicationstory.data.retrofit.ApiService

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun provideApiService(): ApiService {
        return ApiConfig.apiInstant
    }

    fun provideStoryRepository(context: Context, userPreference: UserPreference): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.apiInstant
        return StoryRepository(database, apiService, userPreference)
    }

}