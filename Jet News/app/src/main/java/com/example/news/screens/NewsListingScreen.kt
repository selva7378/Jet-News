package com.example.news.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.news.R
import com.example.news.roomdb.News
import com.example.news.ui.theme.NewsTheme
import com.example.news.viewmodel.NewsUiState
import com.example.news.viewmodel.NewsViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// below code is for route
enum class NewsSreen {
    NEWS_SCREEN,
    WEBVIEW_SCREEN
}

@Composable
fun HomeScreen(
    onNewsClick: () -> Unit,
    navController: NavController,
    newsViewModel: NewsViewModel,
    retryAction: () -> Unit,

    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    when (newsViewModel.newsUiState) {
        is NewsUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is NewsUiState.Success -> NewsScreen(onNewsClick,navController, newsViewModel, modifier)
        is NewsUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(retryAction: () -> Unit,modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    onNewsClick: () -> Unit,
    navController: NavController,
    newsViewModel: NewsViewModel,
    modifier: Modifier = Modifier
) {
    var allNewsList = newsViewModel.allNewsList.collectAsState()
    var searchValue by rememberSaveable {
        mutableStateOf("")
    }
    var isSearchFocesed by rememberSaveable {
        mutableStateOf(false)
    }
    val filterNews: (String) -> Unit = { category ->
        newsViewModel.getNewsByCategory(category)
    }

//    val onClickWebView: (String) -> Unit = { url ->
//        navController.navigate("${NewsSreen.WEBVIEW_SCREEN.name}/$url")
//    }
    Column(modifier = modifier.fillMaxSize()) {


        DockedSearchBar(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            },
            trailingIcon = {
                if(isSearchFocesed){
                    IconButton(
                        onClick = {
                            isSearchFocesed = false
                            searchValue = ""
                            newsViewModel.getAllNews()
                        },
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            },
            query = searchValue,
            onQueryChange = {
                searchValue = it
                newsViewModel.getNewsBySearch(it)
            },
            onSearch = {
                newsViewModel.getNewsBySearch(it)
            },
            active = isSearchFocesed,
            onActiveChange = { isActive ->
                isSearchFocesed = isActive
            },
            modifier = Modifier.height(56.dp).fillMaxWidth()
        ) {
        }

        CatagoryList(
            catagories = newsViewModel.apiCatagoryList,
            filterNews = filterNews
        )

        NewsList(
            onNewsClick = onNewsClick,
            newsList = allNewsList.value,
        )
    }
}

@Composable
fun CatagoryList(
    catagories: List<String>,
    filterNews: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow {
        items(catagories, key = { it }) { catagory ->
            CatagoryCard(catagory = catagory, filterNews = filterNews)
        }
    }
}

@Composable
fun NewsList(
    onNewsClick: () -> Unit,
    newsList: List<News>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(20.dp)
    ) {
        items(newsList, { news -> news.newsId }) { news ->
            NewsCard(
                photoUrl = news.imageUrl,
                webViewUrl = news.readMoreUrl,
                onNewsClick = onNewsClick,
                newsTitle = news.title
            )
        }
    }
}

@Composable
fun CatagoryCard(catagory: String, filterNews: (String) -> Unit, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = Modifier
            .padding(4.dp)
            .width(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = {
            filterNews(catagory)
        }
    ) {
        Text(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.CenterHorizontally),
            text = catagory
        )
    }
}

@Composable
fun NewsCard(
    photoUrl: String,
    newsTitle: String,
    webViewUrl: String,
    onNewsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                onNewsClick()
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {

        Column {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(photoUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
            Text(
                text = newsTitle,
                modifier = Modifier.padding(4.dp)
            )
        }

    }

}

@Preview
@Composable
fun NewsListPreview() {
    NewsTheme {
//        NewsList()
    }
}

@Preview
@Composable
fun CatagoryListPreview() {
    NewsTheme {
        CatagoryList(catagories = listOf(
            "all", "national", "business",
            "sports", "world",
            "politics", "technology", "startup", "entertainment",
            "miscellaneous", "hatke", "science", "automobile"
        ),
            {}
        )
    }
}