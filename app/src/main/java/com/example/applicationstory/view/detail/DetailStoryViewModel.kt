package com.example.applicationstory.view.detail

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.applicationstory.R
import com.example.applicationstory.data.response.DetailStoryResponse
import com.example.applicationstory.data.response.StoryItem
import com.example.applicationstory.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryViewModel(application: Application): AndroidViewModel(application) {

    val detailStory = MutableLiveData<StoryItem>()
    private val errorMessage = MutableLiveData<String>()

    fun setDetailStory(id: String, authorization: String) {
        ApiConfig.apiInstant
            .detailStories(id, authorization)
            .enqueue(object : Callback<DetailStoryResponse> {
                @SuppressLint("NullSafeMutableLiveData")
                override fun onResponse(
                    call: Call<DetailStoryResponse>,
                    response: Response<DetailStoryResponse>
                ) {
                    if (response.isSuccessful) {
                        detailStory.postValue(response.body()?.items)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        errorMessage.postValue("$errorBody")
                    }
                }

                override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                    t.message?.let {
                        errorMessage.postValue(R.string.gagal_memuat_data.toString())
                    }
                }
            })
    }

    fun getDetailStory(): LiveData<StoryItem> {
        return detailStory
    }
}
