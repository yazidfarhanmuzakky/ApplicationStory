package com.example.applicationstory.data.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class StoryResponse(
	val error: Boolean,
	val message: String,
	val listStory: List<StoryResponseItem>
)
@Entity(tableName = "story")
data class StoryResponseItem(

	@PrimaryKey
	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null


)
