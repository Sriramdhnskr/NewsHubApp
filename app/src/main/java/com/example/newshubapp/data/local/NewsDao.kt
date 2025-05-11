package com.example.newshubapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news_articles")
    fun getAllNews(): Flow<List<NewsArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<NewsArticleEntity>)

    @Query("DELETE FROM news_articles")
    suspend fun clearAll()
}