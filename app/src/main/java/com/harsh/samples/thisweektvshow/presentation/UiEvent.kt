package com.harsh.samples.thisweektvshow.presentation

import com.harsh.samples.thisweektvshow.domain.model.TvShow

sealed interface UiEvent {
    data class LoadTvShowDetails(val tvShow: TvShow) : UiEvent
    data class OnFavorite(val tvShow: TvShow) : UiEvent
    data class OnSearchQueryChange(val searchQuery: String) : UiEvent
    data object OnSearchClose : UiEvent
}
