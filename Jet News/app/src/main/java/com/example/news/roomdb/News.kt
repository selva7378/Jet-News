package com.example.news.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "News")
data class News(
    @PrimaryKey(autoGenerate = true)
    var newsId: Int = 0,
    val category: String,
    val author: String,
    val content: String,
    val date: String,
    val id: String,
    val imageUrl: String,
    val readMoreUrl: String,
    val time: String,
    val title: String,
    val url: String,
    val success: String
)
