package com.example.newshubapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.newshubapp.R
import com.example.newshubapp.domain.model.NewsArticle
import com.example.newshubapp.ui.viewmodel.NewsViewModel
import kotlinx.coroutines.delay
import com.example.newshubapp.utils.Result

@Composable
fun MainScreen(newsViewModel: NewsViewModel = viewModel()) {
    val newsState by newsViewModel.newsState.collectAsState()
    val searchQuery by newsViewModel.searchQuery.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChanged = newsViewModel::onSearchQueryChanged
        )

        Spacer(modifier = Modifier.height(12.dp))

        when (newsState) {
            is Result.Loading -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is Result.Success -> {
                val articles = (newsState as Result.Success<List<NewsArticle>>).data
                if (articles.isEmpty()) {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text("No news found", style = MaterialTheme.typography.bodyMedium)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        Log.i("tagger","articles : ${articles}")
                        items(articles, key = { it.id }) { article ->
                           Log.i("tagger","article : ${article.id},  title : ${article.title}")
                            NewsCard(article)
                        }
                    }
                }
            }

            is Result.Error -> {
                val message = (newsState as Result.Error).message
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("Error: $message", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}


@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    var text by remember { mutableStateOf(query) }

    LaunchedEffect(text) {
        delay(300) // debounce
        onQueryChanged(text)
    }

    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Search News") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        colors = TextFieldDefaults.colors()
    )
}

@Composable
fun NewsCard(newsArticle: NewsArticle) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = newsArticle.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            val context = LocalContext.current
            val imageUrl = newsArticle.imageUrl

            if (!imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    placeholder = painterResource(R.drawable.placeholder_image),
                    error = painterResource(R.drawable.error_image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.error_image),
                    contentDescription = "No Image",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = newsArticle.description ?: "No description available",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}





