package com.harsh.samples.thisweektvshow.presentation.composeables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harsh.samples.thisweektvshow.domain.model.TvShowSeason

@Composable
fun SingleSeason(season: TvShowSeason) {
    Row(
        Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                ), shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Text(text = season.name)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "${season.noOfEpisodes} episodes")
    }
}

//TODO: Make ui better

@Preview
@Composable
fun PreviewSingleSeason() {
    val season = TvShowSeason(
        1,
        "Season 1",
        10,
        1,
        8.3f
    )

    SingleSeason(season)
}
