package com.example.applicationstory.data.local

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.applicationstory.data.database.StoryDatabase
import com.example.applicationstory.data.pref.UserPreference
import com.example.applicationstory.data.response.StoryResponseItem
import com.example.applicationstory.data.retrofit.ApiService

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService, private val userPreference: UserPreference) {
    fun getQuote(): LiveData<PagingData<StoryResponseItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, userPreference),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllQuote()
            }
        ).liveData
    }
}