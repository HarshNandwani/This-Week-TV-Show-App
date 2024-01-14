package com.harsh.samples.thisweektvshow.presentation.composeables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.harsh.samples.thisweektvshow.domain.model.TvShow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleTvShow(
    tvShow: TvShow,
    onShowClick: (tvShow: TvShow) -> Unit,
    minimalView: Boolean = false
) {

    Card(
        modifier = Modifier.padding(4.dp).widthIn(max = 100.dp),
        onClick = { onShowClick(tvShow) }
    ) {
        Column {
            SubcomposeAsyncImage(
                model = tvShow.posterUrl,
                contentDescription = "${tvShow.title} poster"
            ) {
                when (painter.state) {
                    AsyncImagePainter.State.Empty -> {  }
                    is AsyncImagePainter.State.Error -> {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = "Error", tint = Color.Red)
                    }
                    is AsyncImagePainter.State.Loading -> { CircularProgressIndicator() }
                    is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
                }
            }

            Spacer(modifier = Modifier.size(4.dp))

            Column(Modifier.padding(8.dp)) {
                Text(
                    text = tvShow.title,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.size(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        modifier = Modifier.size(12.dp)
                    )
                    Text(text = String.format("%.1f", tvShow.voteAvg), style = MaterialTheme.typography.bodySmall)
                    if (tvShow.isFavorite)
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favorite",
                                Modifier.size(12.dp)
                            )
                        }
                }

                if (minimalView) return@Card
                Spacer(modifier = Modifier.size(2.dp))
                Text(text = tvShow.overview, maxLines = 2, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodySmall)
            }

        }
    }
}

@Preview
@Composable
fun PreviewSingleTvShow() {

    val gameOfThronesShow = TvShow(
        1,
        "Game of Thrones",
        "Seven noble families fight for control of the mythical land of Westeros. Friction between the houses leads to full-scale war. All while a very ancient evil awakens in the farthest north. Amidst the war, a neglected military order of misfits, the Night's Watch, is all that stands between the realms of men and icy horrors beyond.",
        "https://image.tmdb.org/t/p/w500/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg",
        "https://image.tmdb.org/t/p/w1280/rIe3PnM6S7IBUmvNwDkBMX0i9EZ.jpg",
        8.4f,
        listOf("Drama", "Action & Adventure"),
        isFavorite = true
    )

    SingleTvShow(tvShow = gameOfThronesShow, onShowClick = {  }, minimalView = true)
}
