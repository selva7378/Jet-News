package com.example.news

import android.app.Application
import com.example.news.NewsApplication
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.news.viewmodel.NewsViewModel

object AppViewModelProvider {

    val Factory = viewModelFactory {

        initializer {
            NewsViewModel(
                newsApplication().container.networkUtils,
                newsApplication().container.newsApiRepository,
                newsApplication().container.newsDbRepository
            )
        }
    }
}


/**
 * Extension function to queries for [Application] object and returns an instance of
 * [NewsApplication].
 */
fun CreationExtras.newsApplication(): NewsApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as NewsApplication)