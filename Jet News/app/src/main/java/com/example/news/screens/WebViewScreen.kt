package com.example.news.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.runtime.getValue
import com.example.news.LocalNewsViewModel


@Composable
fun WebViewScreen(modifier: Modifier = Modifier) {
    val newsViewModel = LocalNewsViewModel.current
    val currentUrl: String by newsViewModel.currUrl.collectAsState()

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                loadUrl(currentUrl)
            }
        },
        update = { webView ->
            webView.loadUrl(currentUrl)
        },
        modifier = modifier
    )
}