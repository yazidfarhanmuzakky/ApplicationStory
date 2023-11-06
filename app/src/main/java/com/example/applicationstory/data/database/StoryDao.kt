package com.example.applicationstory.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.applicationstory.data.response.StoryResponseItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(story: List<StoryResponseItem>)

    @Query("SELECT * FROM story")
    fun getAllQuote(): PagingSource<Int, StoryResponseItem>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}