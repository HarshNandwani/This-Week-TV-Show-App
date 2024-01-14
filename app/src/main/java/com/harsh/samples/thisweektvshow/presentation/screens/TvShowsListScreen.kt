package com.harsh.samples.thisweektvshow.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.harsh.samples.thisweektvshow.domain.model.TvShow
import com.harsh.samples.thisweektvshow.presentation.DataState.*
import com.harsh.samples.thisweektvshow.presentation.UiState
import com.harsh.samples.thisweektvshow.presentation.composeables.AppBarWithSearchView
import com.harsh.samples.thisweektvshow.presentation.composeables.CircularProgressBar
import com.harsh.samples.thisweektvshow.presentation.composeables.ErrorText
import com.harsh.samples.thisweektvshow.presentation.composeables.SingleTvShow
import kotlinx.coroutines.launch

@Composable
fun TvShowsListScreen(
    state: UiState,
    error: String,
    onShowClick: (tvShow: TvShow) -> Unit,
    onSearchTextChange: (String) -> Unit,
    onSearchClose: () -> Unit
) {
    Log.d("Recomposition", "List screen ${state.metaData.dataState}")
    when (state.metaData.dataState) {
        NotRequested -> {  }

        Loading -> {
            CircularProgressBar()
        }

        Success -> {
            TvShowsGrid(
                shows = state.displayTvShows,
                onShowClick = onShowClick,
                searchText = state.searchText,
                onSearchTextChange = onSearchTextChange,
                onSearchClose = onSearchClose,
                error
            )
        }

        Failed -> {
            ErrorText(state.metaData.message ?: "")
        }
    }
}

@Composable
fun TvShowsGrid(
    shows: List<TvShow>,
    onShowClick: (tvShow: TvShow) -> Unit,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClose: () -> Unit,
    error: String
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var title = remember { "Trending TV Shows" }

    Scaffold(
        topBar = {
            AppBarWithSearchView(
                titleText = title,
                searchText = searchText,
                onSearchTextChange = onSearchTextChange,
                onSearchImeClicked = { title = "$searchText search results" },
                onCloseClicked = onSearchClose
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { paddingValues ->
        LazyVerticalGrid(columns = GridCells.Fixed(3), Modifier.padding(paddingValues)) {
            items(shows) { show ->
                SingleTvShow(tvShow = show, onShowClick = onShowClick)
            }
        }
    }

    LaunchedEffect(key1 = error) {
        if (error.isBlank()) return@LaunchedEffect
        coroutineScope.launch {
            snackBarHostState.showSnackbar(error, withDismissAction = true)
        }
    }

}
