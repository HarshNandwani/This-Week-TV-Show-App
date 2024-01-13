package com.harsh.samples.thisweektvshow.presentation.composeables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ErrorText(message: String) {
    Text(
        text = message,
        style = MaterialTheme.typography.titleLarge,
        color = Color.Red
    )
}
