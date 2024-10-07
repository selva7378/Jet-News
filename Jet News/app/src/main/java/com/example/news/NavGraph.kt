package com.example.news

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
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

val ROUTE_USER_DETAILS = "user_details?Data={user}"

//@Composable
//fun NewsApp(
//    newsViewModel: NewsViewModel = viewModel(factory = AppViewModelProvider.Factory),
//    navController: NavHostController = rememberNavController(),
//    modifier: Modifier = Modifier
//) {
//    NavHost(
//        navController = navController,
//        startDestination = NewsSreen.NEWS_SCREEN.name,
//        modifier = modifier
//    ) {
//
//        composable(
//            route = NewsSreen.NEWS_SCREEN.name
//        ) {
//            HomeScreen(
//                navController = navController,
//                newsViewModel = newsViewModel,
//                retryAction = {
//                    newsViewModel.getNewsFromApi()
//                    newsViewModel.getAllNews()
//                }
//            )
//        }
//
//        composable(
//            route = "${NewsSreen.WEBVIEW_SCREEN.name}/{url}",
//            arguments = listOf(navArgument(name = "url") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val url = backStackEntry.arguments?.getString("url")
//            WebViewScreen(
//                url = url ?: "",
//                modifier = modifier
//            )
//        }
//
//    }
//
//}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun NewsAppContent(
    newsViewModel: NewsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Long>()

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
                    newsViewModel = newsViewModel,
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


@Preview(showBackground = true)
@Composable
fun NewsAppPreview() {
    NewsTheme {
        NewsAppContent()
    }
}