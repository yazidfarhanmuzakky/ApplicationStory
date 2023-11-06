package com.example.applicationstory.data.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class StoryPagingResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<StoryResponse>
)
@Entity(tableName = "listStory")
data class StoryResponse(

    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val author: String? = null,

    @field:SerializedName("description")
    val en: String? = null,

    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null

)