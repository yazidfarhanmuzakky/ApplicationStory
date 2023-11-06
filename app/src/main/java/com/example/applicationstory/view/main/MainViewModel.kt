package com.example.applicationstory.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.applicationstory.data.UserRepository
import com.example.applicationstory.data.local.QuoteRepository
import com.example.applicationstory.data.pref.UserModel
import com.example.applicationstory.data.response.QuoteResponseItem

class MainViewModel(storyRepository: QuoteRepository, private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    val story: LiveData<PagingData<QuoteResponseItem>> =
        storyRepository.getQuote().cachedIn(viewModelScope)
}