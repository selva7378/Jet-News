package com.example.news.repository

import androidx.room.Insert
import androidx.room.Query
import com.example.news.roomdb.News
import com.example.news.roomdb.NewsDao
import kotlinx.coroutines.flow.Flow

interface NewsDbRepository {

    suspend fun insert(news: News)

    fun getAll(): Flow<List<News>>

    suspend fun clear()

    fun getAllByCategory(category: String): Flow<List<News>>

    fun getAll(pageSize: Int, offset: Int): Flow<List<News>>

    fun search(query: String): Flow<List<News>>

    suspend fun getDbSize(): Int

}

class OfflineNewsRepository(private val newsDao: NewsDao) : NewsDbRepository {
    override suspend fun insert(news: News) = newsDao.insert(news)

    override fun getAll(): Flow<List<News>> = newsDao.getAll()

    override suspend fun clear() = newsDao.clear()

    override fun getAllByCategory(
        category: String,
    ): Flow<List<News>> = newsDao.getAllByCategory(category)

    override fun getAll(pageSize: Int, offset: Int): Flow<List<News>> = newsDao.getAll()

    override fun search(
        query: String,

    ): Flow<List<News>> = newsDao.search(query)

    override suspend fun getDbSize(): Int = newsDao.getDbSize()
}