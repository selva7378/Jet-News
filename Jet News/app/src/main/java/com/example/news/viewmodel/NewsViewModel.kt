package com.example.news.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.news.network.Data
import com.example.news.network.NewsData
import com.example.news.repository.NewsApiRepository
import com.example.news.repository.NewsDbRepository
import com.example.news.roomdb.News
import com.example.news.util.NetworkUtils
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface NewsUiState {
    object Success : NewsUiState
    object Error : NewsUiState
    object Loading : NewsUiState
}

class NewsViewModel(
    private val networkUtils: NetworkUtils,
    private val newsApiRepository: NewsApiRepository,
    private val newsDbRepository: NewsDbRepository
) : ViewModel() {

    var newsUiState: NewsUiState by mutableStateOf(NewsUiState.Loading)
        private set

    var dbSize = 0

    private val _allNewsList: MutableStateFlow<List<News>> = MutableStateFlow(listOf())
    val allNewsList: StateFlow<List<News>> = _allNewsList

    val apiCatagoryList: List<String> = listOf(
        "all", "national", "business",
        "sports", "world",
        "politics", "technology", "startup", "entertainment",
        "miscellaneous", "hatke", "science", "automobile"
    )

    init {
        Log.i("ViewModel", "ViewModel created")
        viewModelScope.launch {
            initializeData()
        }
    }

    private suspend fun initializeData() {
        Log.i("ViewModel", "initializedDataexecuted")
        setDbSize()
        if (networkUtils.isNetworkAvailable) {
            Log.i("ViewModel", "Network available")
            newsDbRepository.clear()
            getNewsFromApi()
            getAllNews()
        } else {
            Log.i("ViewModel", "Network not available")
            if (dbSize == 0) {
                newsUiState = NewsUiState.Error
            } else {
                getAllNews()
                newsUiState = NewsUiState.Success
            }
        }
    }


    fun getNewsFromApi() {
        Log.i("ViewModel", "getfromApi executed")
        viewModelScope.launch {
            try {
                apiCatagoryList.forEach { category ->
                    val newsData = newsApiRepository.getNews(category)
                    insert(newsData)
                }
                newsUiState = NewsUiState.Success
            } catch (e: IOException) {
                newsUiState = NewsUiState.Error
                Log.e("ViewModel", "Network error due to IOException", e)

            } catch (e: HttpException) {
                Log.e("ViewModel", "HTTP error due to HttpException", e)
                newsUiState = NewsUiState.Error

            } catch (e: Exception) {
                Log.e("ViewModel", "Unexpected error", e)
                newsUiState = NewsUiState.Error

            }
        }
    }

    suspend fun insert(news: NewsData) {
        news.data.forEach { perNews ->
            newsDbRepository.insert(
                News(
                    category = news.category,
                    author = perNews.author,
                    content = perNews.content,
                    date = perNews.date,
                    id = perNews.id,
                    imageUrl = perNews.imageUrl,
                    readMoreUrl = perNews.readMoreUrl,
                    time = perNews.time,
                    title = perNews.title,
                    url = perNews.url,
                    success = news.success.toString()
                )
            )
        }
    }

    fun getAllNews() {
        viewModelScope.launch {
            newsDbRepository.getAll().stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            ).collect { news ->
                _allNewsList.value = news
            }
        }
    }

    fun getNewsBySearch(query: String) {
        if (query.isEmpty()) {
            getAllNews()
        } else {
            viewModelScope.launch {
                newsDbRepository.search(query).stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = emptyList()
                ).collect { news ->
                    _allNewsList.value = news
                }
            }
        }
    }

    fun getNewsByCategory(category: String) {
        viewModelScope.launch {
            if (category == "all") {
                getAllNews()
            } else {
                newsDbRepository.getAllByCategory(category).stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = emptyList()
                ).collect { news ->
                    _allNewsList.value = news
                }
            }
        }
    }

    suspend fun setDbSize() {
        Log.i("ViewModel", "db size = ${newsDbRepository.getDbSize()}")
        dbSize = newsDbRepository.getDbSize()
    }


}