package com.example.applicationstory.data.local

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dicoding.myunlimitedquotes.database.QuoteDatabase
import com.example.applicationstory.data.pref.UserPreference
import com.example.applicationstory.data.response.QuoteResponseItem
import com.example.applicationstory.data.retrofit.ApiService

class QuoteRepository(private val quoteDatabase: QuoteDatabase, private val apiService: ApiService, private val userPreference: UserPreference) {
    fun getQuote(): LiveData<PagingData<QuoteResponseItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = QuoteRemoteMediator(quoteDatabase, apiService, userPreference),
            pagingSourceFactory = {
                quoteDatabase.quoteDao().getAllQuote()
            }
        ).liveData
    }
}