package com.example.news

import android.app.Application
import com.example.news.container.AppContainer
import com.example.news.container.DefaultAppContainer

class NewsApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}