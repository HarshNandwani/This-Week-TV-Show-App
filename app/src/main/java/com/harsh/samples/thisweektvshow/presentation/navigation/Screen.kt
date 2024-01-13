package com.harsh.samples.thisweektvshow.presentation.navigation

sealed class Screen(val route: String) {
    data object ListScreen : Screen("list_screen")
    data object DetailScreen : Screen("detail_screen")
}
