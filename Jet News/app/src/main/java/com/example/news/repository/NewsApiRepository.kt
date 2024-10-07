package com.example.news.repository

import com.example.news.network.NewsApiService
import com.example.news.network.NewsData

interface NewsApiRepository {
    suspend fun getNews(category: String): NewsData
}

class NetworkNewsApiRepository(
    private val   newsApiService: NewsApiService
): NewsApiRepository{
    override suspend fun getNews(category: String): NewsData = newsApiService.getData(category)

}


