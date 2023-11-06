package com.example.applicationstory.formatter

import com.example.applicationstory.data.response.QuoteResponseItem
import com.example.applicationstory.data.response.StoryItem

object DataDummy {
    fun generateDummyNewsEntity(): ArrayList<StoryItem> {
        val newsList = ArrayList<StoryItem>()
        for (i in 0..10) {
            val news = StoryItem(
                "story-wxBx_hv9KdzZXR8G",
                "anthon",
                "Ini Deskripsi",
                "https://story-api.dicoding.dev/images/stories/photos-1698141706208_ClX6Qnma.png",
                "2023-10-24T10:01:46.210Z",
                -7.9987033f,
                112.64299f
            )
            newsList.add(news)
        }
        return newsList
    }

    fun generateDummyQuoteResponse(): List<QuoteResponseItem> {
        val items: MutableList<QuoteResponseItem> = arrayListOf()
        for (i in 0..100) {
            val quote = QuoteResponseItem(
                i.toString(),
                "name + $i",
                "description $i",
                "photoUrl $i",
                "createdAt $i",
            )
            items.add(quote)
        }
        return items
    }
}
