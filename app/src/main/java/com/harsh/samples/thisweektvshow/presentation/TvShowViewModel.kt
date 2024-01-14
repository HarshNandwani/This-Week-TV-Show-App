package com.harsh.samples.thisweektvshow.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.samples.thisweektvshow.domain.model.Source
import com.harsh.samples.thisweektvshow.domain.model.TvShow
import com.harsh.samples.thisweektvshow.domain.use_case.GetSearchedShowsUseCase
import com.harsh.samples.thisweektvshow.domain.use_case.GetShowDetailsUseCase
import com.harsh.samples.thisweektvshow.domain.use_case.GetSimilarShowsUseCase
import com.harsh.samples.thisweektvshow.domain.use_case.GetThisWeekTrendingShowsUseCase
import com.harsh.samples.thisweektvshow.domain.use_case.ToggleShowFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowViewModel @Inject constructor(
    private val getThisWeekTrendingShows: GetThisWeekTrendingShowsUseCase,
    private val getSearchedShows: GetSearchedShowsUseCase,
    private val getShowDetails: GetShowDetailsUseCase,
    private val getSimilarShows: GetSimilarShowsUseCase,
    private val toggleShowFavorite: ToggleShowFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    private var searchedTvShowsJob: Job? = null
    private var loadDetailedTvShowJob: Job? = null
    private var loadSimilarTvShowsJob: Job? = null

    init {
        loadThisWeekTrendingTvShows()
    }

    fun onEvent(event: UiEvent) {
        when(event) {
            is UiEvent.LoadTvShowDetails -> {
                _state.update {
                    it.copy(
                        metaData = it.metaData.copy(detailedDataState = DataState.Loading),
                        detailedTvShow = event.tvShow
                    )
                }
                loadDetailedTvShow(show = event.tvShow)
                loadSimilarTvShows(show = event.tvShow)
            }

            is UiEvent.OnFavorite -> {
                val currentFavStatus = _state.value.detailedTvShow?.isFavorite ?: false
                _state.update { it.copy(detailedTvShow = it.detailedTvShow?.copy(isFavorite = !currentFavStatus)) }
                _state.value.trendingShowsData.tvShows.find { it.id == event.tvShow.id }?.isFavorite = !currentFavStatus
                viewModelScope.launch { toggleShowFavorite(event.tvShow.id, !currentFavStatus) }
            }

            is UiEvent.OnSearchQueryChange -> {
                _state.update { it.copy(searchText = event.searchQuery, displayTvShows = emptyList()) }
                loadSearchedTvShows(event.searchQuery)
                if (event.searchQuery.isBlank()) {
                    _state.update { it.copy(displayTvShows = it.trendingShowsData.tvShows) }
                    searchedTvShowsJob?.cancel()
                }
            }

            UiEvent.OnSearchClose -> {
                // search is done return to viewing trending shows
                _state.update { it.copy(displayTvShows = it.trendingShowsData.tvShows, searchText = "") }
            }

            UiEvent.Refresh -> {
                // Refresh if needed.
                if (_state.value.trendingShowsData.loadedFrom != Source.REMOTE) {
                    loadThisWeekTrendingTvShows()
                }

                if (_state.value.metaData.detailedDataState == DataState.Failed) {
                    _state.value.detailedTvShow?.let { onEvent(UiEvent.LoadTvShowDetails(it)) }
                }
            }
        }
    }

    private fun loadDetailedTvShow(show: TvShow) {
        loadDetailedTvShowJob?.cancel()
        loadDetailedTvShowJob = viewModelScope.launch(Dispatchers.IO) {
            getShowDetails(show)
                .onSuccess { detailedTvShow ->
                    _state.update {
                        it.copy(
                            detailedTvShow = detailedTvShow,
                            metaData = it.metaData.copy(detailedDataState = DataState.Success),
                            displayTvShows = it.trendingShowsData.tvShows
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            metaData = it.metaData.copy(
                                detailedDataState = DataState.Failed,
                                message = exception.message ?: "Something went wrong"
                            )
                        )
                    }
                }
        }
    }

    private fun loadSimilarTvShows(show: TvShow) {
        loadSimilarTvShowsJob?.cancel()
        loadSimilarTvShowsJob = viewModelScope.launch(Dispatchers.IO) {
            getSimilarShows(show)
                .onSuccess { similarTvShows ->
                    _state.update {
                        it.copy(
                            similarTvShows = similarTvShows,
                            metaData = it.metaData.copy(similarTvShowsDataState = DataState.Success)
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            it.metaData.copy(
                                similarTvShowsDataState = DataState.Failed,
                                message = exception.message ?: "Something went wrong"
                            )
                        )
                    }
                }
        }
    }

    private fun loadThisWeekTrendingTvShows() {
        _state.update { it.copy(metaData = it.metaData.copy(dataState = DataState.Loading)) }
        viewModelScope.launch(Dispatchers.IO) {
            getThisWeekTrendingShows()
                .onSuccess { data ->
                    _state.update {
                        it.copy(
                            trendingShowsData = data,
                            displayTvShows = data.tvShows,
                            metaData = it.metaData.copy(dataState = DataState.Success)
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            metaData = it.metaData.copy(
                                dataState = DataState.Failed,
                                message = exception.message ?: "Something went wrong"
                            )
                        )
                    }
                }
        }
    }

    private fun loadSearchedTvShows(searchQuery: String) {
        if(searchQuery.isBlank()) return
        searchedTvShowsJob?.cancel()
        _state.update { it.copy(metaData = it.metaData.copy(searchedTvShowDataState = DataState.Loading)) }
        searchedTvShowsJob = viewModelScope.launch(Dispatchers.IO) {
            delay(1000) // wait for user to type
            getSearchedShows(searchQuery)
                .onSuccess { searchedTvShows ->
                    _state.update { it.copy(
                        searchedTvShows = searchedTvShows,
                        displayTvShows = searchedTvShows,
                        metaData = it.metaData.copy(searchedTvShowDataState = DataState.Success)
                    ) }
                }
                .onFailure {
                    _state.update { it.copy(metaData = it.metaData.copy(searchedTvShowDataState = DataState.Failed)) }
                }
        }
    }
}
