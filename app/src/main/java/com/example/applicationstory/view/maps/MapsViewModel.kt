package com.example.applicationstory.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.applicationstory.data.response.StoriesResponse
import com.example.applicationstory.data.retrofit.ApiConfig

class MapsViewModel : ViewModel() {
    private val listStories = MutableLiveData<StoriesResponse>()
    private val errorMessage = MutableLiveData<String>()

    suspend fun getAllStories(location: Int, authorization: String) {
        try {
            val response = ApiConfig.apiInstant.getStoriesWithLocation(location, authorization)
            if (response.isSuccessful) {
                listStories.postValue(response.body())
            } else {
                errorMessage.postValue("Error ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            errorMessage.postValue("Error: ${e.message}")
        }
    }


    fun getListStories(): LiveData<StoriesResponse> {
        return listStories
    }
}
