package com.harsh.samples.thisweektvshow.presentation.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.harsh.samples.thisweektvshow.domain.model.TvShowSeason
import com.harsh.samples.thisweektvshow.presentation.composeables.SingleSeason

@Composable
fun SeasonsList(seasons: List<TvShowSeason>) {
    Text(text = "List of seasons", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.size(2.dp))
    LazyColumn {
        items(seasons) { tvShowSeason ->
            SingleSeason(season = tvShowSeason)
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}
