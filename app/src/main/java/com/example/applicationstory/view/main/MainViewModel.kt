package com.example.applicationstory.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.applicationstory.data.UserRepository
import com.example.applicationstory.data.local.StoryRepository
import com.example.applicationstory.data.pref.UserModel
import com.example.applicationstory.data.response.StoryResponseItem

class MainViewModel(storyRepository: StoryRepository, private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    val story: LiveData<PagingData<StoryResponseItem>> =
        storyRepository.getQuote().cachedIn(viewModelScope)
}