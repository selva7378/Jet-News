package com.example.news.network

import kotlinx.serialization.Serializable

@Serializable
data class NewsData(
    val category: String,
    val `data`: List<Data>,
    val success: Boolean
)
