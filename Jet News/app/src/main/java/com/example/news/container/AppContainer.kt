package com.example.news.container

import android.app.Application
import android.content.Context
import com.example.news.network.NewsApiService
import com.example.news.repository.NetworkNewsApiRepository
import com.example.news.repository.NewsApiRepository
import com.example.news.repository.NewsDbRepository
import com.example.news.repository.OfflineNewsRepository
import com.example.news.roomdb.NewsDatabase
import com.example.news.util.NetworkUtils
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val newsApiRepository: NewsApiRepository
    val newsDbRepository: NewsDbRepository
    val networkUtils: NetworkUtils
}


class DefaultAppContainer(private val context: Context) : AppContainer {

    private val baseUrl =
        "https://inshortsapi.vercel.app/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }

    override val newsApiRepository: NewsApiRepository by lazy {
        NetworkNewsApiRepository(retrofitService)
    }
    override val networkUtils: NetworkUtils
        get() = NetworkUtils(context)

    override val newsDbRepository: NewsDbRepository by lazy {
        OfflineNewsRepository(NewsDatabase.getDatabase(context).newsDao())
    }
}