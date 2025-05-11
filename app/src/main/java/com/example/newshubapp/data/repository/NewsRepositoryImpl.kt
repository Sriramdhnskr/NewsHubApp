package com.example.newshubapp.data.repository

import android.util.Log
import com.example.newshubapp.data.local.NewsDao
import com.example.newshubapp.data.mapper.toDomain
import com.example.newshubapp.data.mapper.toEntity
import com.example.newshubapp.data.remote.NewsApiService
import com.example.newshubapp.domain.model.NewsArticle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApiService,
    private val dao: NewsDao,
    @Named("apiKey") private val apiKey: String,
    @Named("language") private val language: String
) : NewsRepository {

    override val newsFlow: Flow<List<NewsArticle>> = dao.getAllNews().map { it.map { it.toDomain() } }

    override suspend fun refreshNews() {
        try {
            val response = api.getNews(apiKey, language)
            Log.d("response", "Received")

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.results.isNotEmpty()) {
                    val ids = body.results.map { it.article_id }

                    Log.d("resids", "Response ids : ${ids}")
                    Log.d("response", "Received ${body.results.size} articles")

                    // Persist data
                    dao.clearAll()
                    dao.insertAll(body.results.map { it.toEntity() })  // use DTO → Entity mapper here
                } else {
                    Log.w("response", "Empty or null body in response")
                }
            } else {
                Log.e("response", "API error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("response", "Network or parsing error", e)
        }
    }

    override suspend fun search(query: String): List<NewsArticle> {
        Log.i("sizeflow","size : ${newsFlow.first().size}")
        return newsFlow.first().filter {
            Log.i("tagger","newsflow : ${it}")
            it.title.contains(query, true) || (it.description?.contains(query, true) == true)
        }
    }
}