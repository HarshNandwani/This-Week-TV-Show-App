package com.harsh.samples.thisweektvshow.presentation

import com.harsh.samples.thisweektvshow.domain.model.TvShow

sealed interface UiEvent {
    data object LoadMoreTrendingTvShows: UiEvent
    data class LoadTvShowDetails(val tvShow: TvShow, val newlyAdded: Boolean = true) : UiEvent
    data class OnFavorite(val tvShow: TvShow) : UiEvent
    data class OnSearchQueryChange(val searchQuery: String) : UiEvent
    data object OnSearchClose : UiEvent
    data object Refresh : UiEvent
    data object RetrieveLastDetailTvShow : UiEvent
}
