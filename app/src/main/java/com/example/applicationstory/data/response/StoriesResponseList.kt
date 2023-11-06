package com.example.applicationstory.data.response

import com.google.gson.annotations.SerializedName

data class StoriesResponseList(
    @SerializedName("listStory")
    val error: Boolean,
    val message: String,
    val items : List<StoryResponseItem>
)