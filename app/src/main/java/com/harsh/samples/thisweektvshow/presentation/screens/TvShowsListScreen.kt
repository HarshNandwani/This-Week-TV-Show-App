package com.harsh.samples.thisweektvshow.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.harsh.samples.thisweektvshow.domain.model.TvShow
import com.harsh.samples.thisweektvshow.presentation.DataState.*
import com.harsh.samples.thisweektvshow.presentation.UiState
import com.harsh.samples.thisweektvshow.presentation.composeables.AppBarWithSearchView
import com.harsh.samples.thisweektvshow.presentation.composeables.CircularProgressBar
import com.harsh.samples.thisweektvshow.presentation.composeables.ErrorText
import com.harsh.samples.thisweektvshow.presentation.composeables.SingleTvShow
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun TvShowsListScreen(
    state: UiState,
    error: String,
    loadMoreShows: () -> Unit,
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
                state = state,
                error = error,
                loadMoreShows = loadMoreShows,
                onShowClick = onShowClick,
                onSearchTextChange = onSearchTextChange,
                onSearchClose = onSearchClose
            )
        }

        Failed -> {
            ErrorText(state.metaData.message ?: "")
        }
    }
}

@Composable
fun TvShowsGrid(
    state: UiState,
    error: String,
    loadMoreShows: () -> Unit,
    onShowClick: (tvShow: TvShow) -> Unit,
    onSearchTextChange: (String) -> Unit,
    onSearchClose: () -> Unit,
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val lazyGridState = rememberLazyGridState()
    val firstVisibleItemIndex = remember { derivedStateOf { lazyGridState.firstVisibleItemIndex }  }

    Scaffold(
        topBar = {
            AppBarWithSearchView(
                titleText = "Trending TV Shows",
                searchText = state.searchText,
                onSearchTextChange = onSearchTextChange,
                onSearchImeClicked = {  },
                onCloseClicked = onSearchClose
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            state = lazyGridState,
            modifier = Modifier.padding(paddingValues)
        ) {
            items(
                items = state.displayTvShows,
                key = { tvShow ->
                    /*
                    * Temporary work around, the api is sending below shows in multiple pages, this is an issue at API end.
                    * duplicate keys aren't allowed so using random.
                    * If random is used as key everytime the scrolling is not smooth, hence this!
                    *
                    * I have handled up-to page 20, if app crashes after loading 20 pages its definitely because API is
                    * sending shows again in multiple pages.
                    * */
                    val duplicateShowIds = listOf(12971L, 1402L, 46298L, 3034L, 62745L, 78102L)
                    if (tvShow.id in duplicateShowIds)
                        Random.nextInt()
                    else
                        tvShow.id
                }
            ) { show ->
                SingleTvShow(tvShow = show, onShowClick = onShowClick)
            }

            item {
                when (state.metaData.moreTvShowsDataState) {
                    NotRequested ->  {  }
                    Loading -> { CircularProgressIndicator() }
                    Success -> { /*do nothing*/ }
                    Failed -> {
                        Text(text = state.metaData.message ?: "Cannot load more shows", color = Color.Red)
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = error) {
        if (error.isBlank()) return@LaunchedEffect
        coroutineScope.launch {
            snackBarHostState.showSnackbar(error, withDismissAction = true)
        }
    }

    if (firstVisibleItemIndex.value >= state.displayTvShows.size - 9) {
        loadMoreShows()
    }
}
