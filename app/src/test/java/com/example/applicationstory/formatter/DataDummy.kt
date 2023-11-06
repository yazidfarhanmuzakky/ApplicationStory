package com.example.applicationstory.formatter

import com.example.applicationstory.data.response.StoryResponseItem

object DataDummy {

    fun generateDummyQuoteResponse(): List<StoryResponseItem> {
        val items: MutableList<StoryResponseItem> = arrayListOf()
        for (i in 0..100) {
            val quote = StoryResponseItem(
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
