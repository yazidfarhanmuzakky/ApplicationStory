package com.example.applicationstory.data.response

import com.google.gson.annotations.SerializedName

data class AddStoryResponse (
    @field:SerializedName("message")
    val message: String
)