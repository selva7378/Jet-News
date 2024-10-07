package com.example.news.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApiService {
    @GET("/news")
    suspend fun getData(@Query("category") category: String): NewsData

}