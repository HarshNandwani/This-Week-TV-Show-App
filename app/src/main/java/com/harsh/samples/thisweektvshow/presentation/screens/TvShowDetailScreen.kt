package com.harsh.samples.thisweektvshow.presentation.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.harsh.samples.thisweektvshow.R
import com.harsh.samples.thisweektvshow.domain.model.TvShow
import com.harsh.samples.thisweektvshow.domain.model.TvShowSeason
import com.harsh.samples.thisweektvshow.presentation.DataState.*
import com.harsh.samples.thisweektvshow.presentation.UiState
import com.harsh.samples.thisweektvshow.presentation.composeables.CircularProgressBar
import com.harsh.samples.thisweektvshow.presentation.composeables.ErrorText

@Composable
fun TvShowDetailScreen(state: UiState, onBackPress: () -> Unit) {
    Log.d("Recomposition", "Details screen ${state.metaData.detailedDataState}")
    when (state.metaData.detailedDataState) {
        NotRequested -> {  }

        Loading -> {
            CircularProgressBar()
        }

        Success -> {
            state.detailedTvShow?.let { DetailedTvShow(tvShow = it, onBackPress = onBackPress) }
        }

        Failed -> {
            ErrorText(message = state.metaData.message ?: "Something went wrong")
        }
    }

    BackHandler {
        onBackPress()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedTvShow(tvShow: TvShow, onBackPress: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = tvShow.title) },
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
                        Icon(painter = painterResource(id = R.drawable.favorite_outline), contentDescription = "search")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            AsyncImage(
                model = tvShow.backdropUrl,
                contentDescription = "cover image"
            )

            Column(Modifier.padding(16.dp)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Genres: ", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = tvShow.genres.toString())
                }

                Spacer(modifier = Modifier.size(6.dp))
                Text(text = "Overview", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.size(2.dp))
                Text(text = tvShow.overview, textAlign = TextAlign.Justify)

                Spacer(modifier = Modifier.size(6.dp))
                SeasonsList(seasons = tvShow.seasons)

            }
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

    TvShowDetailScreen(UiState(detailedTvShow = gameOfThronesShow), onBackPress = {  })
}
