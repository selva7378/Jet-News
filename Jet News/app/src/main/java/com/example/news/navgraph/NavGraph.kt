package com.example.news.navgraph

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.news.AppViewModelProvider
import com.example.news.screens.HomeScreen
import com.example.news.screens.WebViewScreen
import com.example.news.ui.theme.NewsTheme
import com.example.news.viewmodel.NewsViewModel

val LocalNewsViewModel = compositionLocalOf<NewsViewModel> { error("No NewsViewModel found!") }

@Composable
fun NewsApp(
    modifier: Modifier
) {
    NewsNavigationWrapperUI {
        NewsAppContent(
            modifier = modifier
        )
    }
}

@Composable
private fun NewsNavigationWrapperUI(
    content: @Composable () -> Unit = {}
) {
    var selectedDestination: NewsDestination by remember {
        mutableStateOf(NewsDestination.News)
    }

    val windowSize = with(LocalDensity.current) {
        currentWindowSize().toSize().toDpSize()
    }
    val layoutType = if (windowSize.width >= 1200.dp) {
        NavigationSuiteType.NavigationDrawer
    } else {
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
            currentWindowAdaptiveInfo()
        )
    }

    NavigationSuiteScaffold(
        layoutType = layoutType,
        navigationSuiteItems = {
            NewsDestination.entries.forEach {
                item(
                    selected = it == selectedDestination,
                    onClick = { selectedDestination = it},
                    icon = {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = stringResource(it.labelRes)
                        )
                    },
                    label = {
                        Text(text = stringResource(it.labelRes))
                    },
                )
            }
        }
    ) {
        content()
    }
}


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