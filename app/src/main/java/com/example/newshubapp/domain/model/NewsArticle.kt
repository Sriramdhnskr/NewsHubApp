package com.example.newshubapp.domain.model

data class NewsArticle(
    val id : String,
    val title: String,
    val description: String?,
    val imageUrl: String?,
)
