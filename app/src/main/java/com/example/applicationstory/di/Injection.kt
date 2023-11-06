package com.example.applicationstory.di

import android.content.Context
import android.content.SharedPreferences
import com.dicoding.myunlimitedquotes.database.QuoteDatabase
import com.example.applicationstory.data.UserRepository
import com.example.applicationstory.data.local.QuoteRepository
//import com.example.applicationstory.data.local.StoryRepository
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

    fun provideStoryRepository(context: Context, userPreference: UserPreference): QuoteRepository {
        val database = QuoteDatabase.getDatabase(context)
        val apiService = ApiConfig.apiInstant
        return QuoteRepository(database, apiService, userPreference)
    }

}