package com.harsh.samples.thisweektvshow.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.harsh.samples.thisweektvshow.presentation.navigation.NavGraph
import com.harsh.samples.thisweektvshow.presentation.theme.ThisWeekTVShowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowActivity : ComponentActivity() {

    private val viewModel: TvShowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThisWeekTVShowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        navHostController = navController,
                        viewModel = viewModel
                    )

                }
            }
        }
    }
}
