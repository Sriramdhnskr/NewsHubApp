package com.example.newshubapp.data.repository

import com.example.newshubapp.domain.model.NewsArticle
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    val newsFlow: Flow<List<NewsArticle>>
    suspend fun refreshNews()
    suspend fun search(query: String): List<NewsArticle>
}