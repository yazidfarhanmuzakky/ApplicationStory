package com.example.applicationstory.data.response

import com.google.gson.annotations.SerializedName

data class DetailStoryResponse(
    @SerializedName("story")
    val items : StoryItem
)