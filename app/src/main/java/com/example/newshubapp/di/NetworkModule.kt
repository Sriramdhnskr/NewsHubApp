package com.example.newshubapp.di

import android.content.Context
import androidx.room.Room
import com.example.newshubapp.data.local.NewsDao
import com.example.newshubapp.data.local.NewsDatabase
import com.example.newshubapp.data.remote.NewsApiService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS) // connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // write timeout
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApiService(): NewsApiService {
        return Retrofit.Builder()
            .baseUrl("https://newsdata.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()
            .create(NewsApiService::class.java)
    }

    @Provides
    @Named("apiKey")
    fun provideApiKey(): String = "pub_86037a8bd84854434c0c3ed9fef1b727ce083"

    @Provides
    @Named("language")
    fun provideLanguage(): String = "en"

    @Provides
    fun provideNewsDao(appDatabase: NewsDatabase): NewsDao {
        return appDatabase.newsDao()
    }

    @Provides
    @Singleton
    fun provideNewsDatabase( @ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(context, NewsDatabase::class.java, "news.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}
