package com.example.newshubapp.data.mapper

import com.example.newshubapp.data.local.NewsArticleEntity
import com.example.newshubapp.data.remote.dto.NewsArticleDTO
import com.example.newshubapp.domain.model.NewsArticle
// DTO → Entity
fun NewsArticleDTO.toEntity(): NewsArticleEntity {
    return NewsArticleEntity(
        id = article_id,
        title = title,
        description = description,
        imageUrl = image_url
    )
}

// Entity → Domain
fun NewsArticleEntity.toDomain(): NewsArticle {
    return NewsArticle(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl
    )
}
