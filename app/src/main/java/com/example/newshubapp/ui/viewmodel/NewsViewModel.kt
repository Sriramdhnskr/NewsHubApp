package com.example.newshubapp.ui.viewmodel

import com.example.newshubapp.domain.GetNewsUseCase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newshubapp.domain.model.NewsArticle
import com.example.newshubapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val useCase: GetNewsUseCase
) : ViewModel() {

    private val _newsState = MutableStateFlow<Result<List<NewsArticle>>>(Result.Loading)
    val newsState: StateFlow<Result<List<NewsArticle>>> = _newsState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        loadNews()
    }

    fun loadNews() {
        viewModelScope.launch {
            _newsState.value = Result.Loading
            try {
                useCase.refreshNews()
                val news = useCase.execute()
                _newsState.value = Result.Success(news)
            } catch (e: Exception) {
                _newsState.value = Result.Error("Failed to load news: ${e.localizedMessage}")
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _newsState.value = Result.Loading
            try {
                val result = useCase.search(query)
                _newsState.value = Result.Success(result)
            } catch (e: Exception) {
                _newsState.value = Result.Error("Search failed: ${e.localizedMessage}")
            }
        }
    }
}


