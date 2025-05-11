package com.example.newshubapp.di

import com.example.newshubapp.data.local.NewsDao
import com.example.newshubapp.data.remote.NewsApiService
import com.example.newshubapp.data.repository.NewsRepository
import com.example.newshubapp.data.repository.NewsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNewsRepository(
        api: NewsApiService,
        dao: NewsDao,
        @Named("apiKey") apiKey: String,
        @Named("language") language: String
    ): NewsRepository {
        return NewsRepositoryImpl(api, dao, apiKey, language)
    }
}
