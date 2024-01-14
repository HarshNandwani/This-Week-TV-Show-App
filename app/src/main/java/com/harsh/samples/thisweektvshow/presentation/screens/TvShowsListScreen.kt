package com.harsh.samples.thisweektvshow.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
    when (state.metaData.dataState) {
        NotRequested -> {  }

        Loading -> {
            CircularProgressBar()
        }

        Success -> {
            TvShowsGrid(shows = state.data.data, onShowClick = onShowClick)
        }

        Failed -> {
            ErrorText(state.metaData.message ?: "")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowsGrid(shows: List<TvShow>, onShowClick: (tvShow: TvShow) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Trending TV Shows") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                    }
                }
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
