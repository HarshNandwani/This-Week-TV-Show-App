package com.harsh.samples.thisweektvshow.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.harsh.samples.thisweektvshow.domain.model.TvShow
import com.harsh.samples.thisweektvshow.presentation.DataState.*
import com.harsh.samples.thisweektvshow.presentation.UiState
import com.harsh.samples.thisweektvshow.presentation.composeables.CircularProgressBar
import com.harsh.samples.thisweektvshow.presentation.composeables.ErrorText
import com.harsh.samples.thisweektvshow.presentation.composeables.SingleTvShow

@Composable
fun TvShowsListScreen(state: UiState, onShowClick: (tvShow: TvShow) -> Unit) {
    Log.d("Recomposition", "List screen ${state.metaData.dataState}")
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        when (state.metaData.dataState) {
            NotRequested -> {

            }

            Loading -> {
                CircularProgressBar()
            }

            Success -> {
                TvShowsGrid(shows = state.data, onShowClick = onShowClick)
            }

            Failed -> {
                ErrorText(state.metaData.message ?: "")
            }
        }
    }
}

@Composable
fun TvShowsGrid(shows: List<TvShow>, onShowClick: (tvShow: TvShow) -> Unit) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(shows) {show->
            SingleTvShow(tvShow = show, onShowClick = onShowClick)
        }
    }
}
