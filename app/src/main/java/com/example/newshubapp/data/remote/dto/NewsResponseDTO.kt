package com.example.newshubapp.data.remote.dto

data class NewsResponseDTO(
    val nextPage: String,
    val results: List<NewsArticleDTO>,
    val status: String,
    val totalResults: Int
)