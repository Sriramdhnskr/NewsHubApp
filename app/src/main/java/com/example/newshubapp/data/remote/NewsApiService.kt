package com.example.newshubapp.data.remote

import com.example.newshubapp.data.remote.dto.NewsResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("api/1/news")
    suspend fun getNews(
        @Query("apikey") apiKey: String,
        @Query("language") language: String = "en"
    ): Response<NewsResponseDTO>
}