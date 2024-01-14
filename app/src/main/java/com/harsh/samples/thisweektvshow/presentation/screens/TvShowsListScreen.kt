package com.harsh.samples.thisweektvshow.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.harsh.samples.thisweektvshow.domain.model.TvShow
import com.harsh.samples.thisweektvshow.presentation.DataState.*
import com.harsh.samples.thisweektvshow.presentation.UiState
import com.harsh.samples.thisweektvshow.presentation.composeables.AppBarWithSearchView
import com.harsh.samples.thisweektvshow.presentation.composeables.CircularProgressBar
import com.harsh.samples.thisweektvshow.presentation.composeables.ErrorText
import com.harsh.samples.thisweektvshow.presentation.composeables.SingleTvShow

@Composable
fun TvShowsListScreen(
    state: UiState,
    onShowClick: (tvShow: TvShow) -> Unit,
    onSearchTextChange: (String) -> Unit,
    onSearchDone: () -> Unit
) {
    Log.d("Recomposition", "List screen ${state.metaData.dataState}")
    when (state.metaData.dataState) {
        NotRequested -> {  }

        Loading -> {
            CircularProgressBar()
        }

        Success -> {
            TvShowsGrid(
                shows = state.data.tvShows,
                onShowClick = onShowClick,
                searchText = state.searchText,
                onSearchTextChange = onSearchTextChange,
                onSearchDone = onSearchDone
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
    onSearchDone: () -> Unit
) {
    Scaffold(
        topBar = {
            AppBarWithSearchView(
                titleText = "Trending TV Shows",
                searchText = searchText,
                onSearchTextChange = onSearchTextChange,
                onSearchImeClicked = onSearchDone
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(columns = GridCells.Fixed(2), Modifier.padding(paddingValues)) {
            items(shows) { show ->
                SingleTvShow(tvShow = show, onShowClick = onShowClick)
            }
        }
    }

}
