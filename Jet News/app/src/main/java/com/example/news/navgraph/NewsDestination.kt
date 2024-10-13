package com.example.news.navgraph

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.People
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.news.R


/** Navigation destinations in the app. */
enum class NewsDestination(
    @StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    News(R.string.tab_news, Icons.Default.Inbox),

    Weather(R.string.tab_weather, Icons.Default.Article),
}
