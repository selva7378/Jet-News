package com.example.news

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.news.screens.HomeScreen
import com.example.news.screens.NewsScreen
import com.example.news.screens.NewsSreen
import com.example.news.screens.WebViewScreen
import com.example.news.ui.theme.NewsTheme
import com.example.news.viewmodel.NewsViewModel

val LocalNewsViewModel = compositionLocalOf<NewsViewModel> { error("No NewsViewModel found!") }


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun NewsAppContent(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val newsViewModel: NewsViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val navigator = rememberListDetailPaneScaffoldNavigator<Long>()

    CompositionLocalProvider(LocalNewsViewModel provides newsViewModel) {
        BackHandler(navigator.canNavigateBack()) {
            navigator.navigateBack()
        }

        ListDetailPaneScaffold(
            modifier = modifier,
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane {
                    HomeScreen(
                        navController = navController,
                        retryAction = {
                            newsViewModel.getNewsFromApi()
                            newsViewModel.getAllNews()
                        },
                        onNewsClick = {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                        }
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    WebViewScreen(
                        modifier = Modifier
                    )
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun NewsAppPreview() {
    NewsTheme {
        NewsAppContent()
    }
}