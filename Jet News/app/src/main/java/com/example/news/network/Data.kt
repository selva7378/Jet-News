package com.example.news.network

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val author: String,
    val content: String,
    val date: String,
    val id: String,
    val imageUrl: String,
    val readMoreUrl: String,
    val time: String,
    val title: String,
    val url: String
)