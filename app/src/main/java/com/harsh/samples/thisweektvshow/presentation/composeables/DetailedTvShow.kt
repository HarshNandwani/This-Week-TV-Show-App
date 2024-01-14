package com.harsh.samples.thisweektvshow.presentation.composeables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.harsh.samples.thisweektvshow.domain.model.TvShow

@Composable
fun DetailedTvShow(tvShow: TvShow) {
    Column {
        SubcomposeAsyncImage(
            model = tvShow.backdropUrl,
            contentDescription = "${tvShow.title} cover image"
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

        Column(Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Genres: ", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = tvShow.genres.toString().removePrefix("[").removeSuffix("]"),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.size(6.dp))
            Text(text = "Overview", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.size(2.dp))
            Text(
                text = tvShow.overview,
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}
