package com.harsh.samples.thisweektvshow.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.harsh.samples.thisweektvshow.domain.model.TvShow
import com.harsh.samples.thisweektvshow.presentation.DataState.*
import com.harsh.samples.thisweektvshow.presentation.UiState
import com.harsh.samples.thisweektvshow.presentation.composeables.SingleTvShow

@Composable
fun TvShowsListScreen(state: UiState) {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        when (state.metaData.dataState) {
            NotRequested -> {

            }

            Loading -> {
                CircularProgressBar()
            }

            Success -> {
                TvShowsGrid(shows = state.data)
            }

            Failed -> {
                ErrorText(state.metaData.message ?: "")
            }
        }
    }
}

@Composable
fun TvShowsGrid(shows: List<TvShow>) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(shows) {show->
            SingleTvShow(tvShow = show)
        }
    }
}

@Composable
fun CircularProgressBar() {
    CircularProgressIndicator(modifier = Modifier.width(64.dp))
}

@Composable
fun ErrorText(message: String) {
    Text(
        text = message,
        style = MaterialTheme.typography.titleLarge,
        color = Color.Red
    )
}
