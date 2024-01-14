package com.harsh.samples.thisweektvshow.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.harsh.samples.thisweektvshow.presentation.TvShowViewModel
import com.harsh.samples.thisweektvshow.presentation.UiEvent
import com.harsh.samples.thisweektvshow.presentation.screens.TvShowDetailScreen
import com.harsh.samples.thisweektvshow.presentation.screens.TvShowsListScreen

@Composable
fun NavGraph(
    navHostController: NavHostController,
    viewModel: TvShowViewModel
) {

    val state = viewModel.state.collectAsStateWithLifecycle().value

    NavHost(navController = navHostController, startDestination = Screen.ListScreen.route) {
        composable(route = Screen.ListScreen.route) {
            TvShowsListScreen(
                state,
                viewModel.error.collectAsStateWithLifecycle().value,
                onShowClick = { tvShow ->
                    viewModel.onEvent(UiEvent.LoadTvShowDetails(tvShow))
                    navHostController.navigate(route = Screen.DetailScreen.route)
                },
                onSearchTextChange = { updatedSearchQuery ->
                    viewModel.onEvent(UiEvent.OnSearchQueryChange(updatedSearchQuery))
                },
                onSearchClose = {
                    viewModel.onEvent(UiEvent.OnSearchClose)
                }
            )
        }

        composable(route = Screen.DetailScreen.route) {
            TvShowDetailScreen(
                state,
                onFavorite = { viewModel.onEvent(UiEvent.OnFavorite(it)) } ,
                onShowClick = { tvShow ->
                    viewModel.onEvent(UiEvent.LoadTvShowDetails(tvShow))
                    navHostController.navigate(route = Screen.DetailScreen.route)
                },
                onBackPress = {
                    viewModel.onEvent(UiEvent.RetrieveLastDetailTvShow)
                    navHostController.popBackStack()
                },
            )
        }
    }
}