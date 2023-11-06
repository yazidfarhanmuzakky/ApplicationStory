package com.example.applicationstory.data.response

import com.google.gson.annotations.SerializedName

data class StoriesResponse(
    @SerializedName("listStory")
    val items : ArrayList<StoryItem>
)