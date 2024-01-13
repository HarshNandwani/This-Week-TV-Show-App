package com.harsh.samples.thisweektvshow.presentation.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harsh.samples.thisweektvshow.R
import com.harsh.samples.thisweektvshow.domain.model.TvShow
import com.harsh.samples.thisweektvshow.domain.model.TvShowSeason
import com.harsh.samples.thisweektvshow.presentation.DataState.*
import com.harsh.samples.thisweektvshow.presentation.UiState
import com.harsh.samples.thisweektvshow.presentation.composeables.CircularProgressBar
import com.harsh.samples.thisweektvshow.presentation.composeables.DetailedTvShow
import com.harsh.samples.thisweektvshow.presentation.composeables.ErrorText
import com.harsh.samples.thisweektvshow.presentation.composeables.SingleSeason

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowDetailScreen(state: UiState, onBackPress: () -> Unit) {
    Log.d("Recomposition", "Details screen ${state.metaData.detailedDataState}")
    when (state.metaData.detailedDataState) {
        NotRequested -> { return }

        Loading -> {
            CircularProgressBar()
            return
        }

        Success -> {}

        Failed -> {
            ErrorText(message = state.metaData.message ?: "Something went wrong")
            return
        }
    }

    BackHandler {
        onBackPress()
    }

    if (state.metaData.detailedDataState != Success) return
    if (state.detailedTvShow == null) return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = state.detailedTvShow.title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { onBackPress() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.favorite_outline),
                            contentDescription = "favorite"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        DetailedTvShowWithSeasons(
            tvShow = state.detailedTvShow,
            modifier = Modifier.padding(paddingValues)
        )
    }

}

@Composable
fun DetailedTvShowWithSeasons(tvShow: TvShow, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        item {
            DetailedTvShow(tvShow = tvShow)
            Text(text = "List of seasons", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
        }

        items(tvShow.seasons) { tvShowSeason ->
            SingleSeason(season = tvShowSeason)
        }
    }

}


//TODO: Make ui better!

@Preview
@Composable
fun PreviewTvShowDetailScreen() {
    val gameOfThronesShow = TvShow(
        1,
        "Game of Thrones",
        "Seven noble families fight for control of the mythical land of Westeros. Friction between the houses leads to full-scale war. All while a very ancient evil awakens in the farthest north. Amidst the war, a neglected military order of misfits, the Night's Watch, is all that stands between the realms of men and icy horrors beyond.",
        "https://image.tmdb.org/t/p/w500/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg",
        "https://image.tmdb.org/t/p/w1280/rIe3PnM6S7IBUmvNwDkBMX0i9EZ.jpg",
        8.4f,
        listOf("Drama", "Action & Adventure"),
        listOf(
            TvShowSeason(1, "Season 1", 10, 1, 8.3f),
            TvShowSeason(2, "Season 2", 10, 2, 8.3f),
            TvShowSeason(3, "Season 3", 10, 3, 8.3f),
            TvShowSeason(4, "Season 4", 10, 4, 8.3f),
            TvShowSeason(5, "Season 5", 10, 5, 8.3f),
            TvShowSeason(6, "Season 6", 10, 6, 8.3f),
            TvShowSeason(7, "Season 7", 10, 7, 8.3f),
            TvShowSeason(8, "Season 8", 10, 8, 8.3f),
            TvShowSeason(9, "Season 9", 10, 9, 8.3f),
            TvShowSeason(10, "Season 10", 10, 10, 8.3f),
        )
    )

    TvShowDetailScreen(UiState(detailedTvShow = gameOfThronesShow), onBackPress = { })
}
