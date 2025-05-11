package com.example.newshubapp.domain

import android.util.Log
import com.example.newshubapp.data.repository.NewsRepository
import com.example.newshubapp.domain.model.NewsArticle
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetNewsUseCase  @Inject constructor(private val repository: NewsRepository) {
    suspend fun execute(): List<NewsArticle> {
        val a = repository.newsFlow.first()
        Log.d("execute", "a called ids: ${a.map { it.id }}")
        return a
    }

    suspend fun refreshNews() {
        Log.d("response", "Refresh news called")
        repository.refreshNews()
    }

    suspend fun search(query: String): List<NewsArticle> {
        return repository.search(query)
    }
}
