package com.example.news.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao{
    @Insert
    suspend fun insert(news: News)

    @Query("SELECT * FROM News")
    fun getAll(): Flow<List<News>>

    @Query("DELETE FROM News")
    suspend fun clear()

    @Query("SELECT * FROM News WHERE category = :category")
    fun getAllByCategory(category: String): Flow<List<News>>

    @Query("SELECT * FROM News LIMIT :pageSize OFFSET :offset")
    fun getAll(pageSize: Int, offset: Int): Flow<List<News>>

    @Query("SELECT * FROM News WHERE title LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<News>>

    @Query("SELECT COUNT(*) FROM News")
    suspend fun getDbSize(): Int
}