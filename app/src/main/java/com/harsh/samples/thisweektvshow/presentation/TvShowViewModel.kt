package com.harsh.samples.thisweektvshow.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.samples.thisweektvshow.domain.model.TvShow
import com.harsh.samples.thisweektvshow.domain.use_case.GetShowDetailsUseCase
import com.harsh.samples.thisweektvshow.domain.use_case.GetSimilarShowsUseCase
import com.harsh.samples.thisweektvshow.domain.use_case.GetThisWeekTrendingShowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowViewModel @Inject constructor(
    private val getThisWeekTrendingShows: GetThisWeekTrendingShowsUseCase,
    private val getShowDetails: GetShowDetailsUseCase,
    private val getSimilarShows: GetSimilarShowsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    private var loadDetailedTvShowJob: Job? = null
    private var loadSimilarTvShowsJob: Job? = null

    init {
        loadThisWeekTrendingTvShows()
    }

    fun onEvent(event: UiEvent) {
        when(event) {
            is UiEvent.LoadTvShowDetails -> {
                _state.value = _state.value.copy(
                    metaData = _state.value.metaData.copy(detailedDataState = DataState.Loading)
                )
                loadDetailedTvShow(show = event.tvShow)
                loadSimilarTvShows(show = event.tvShow)
            }
        }
    }


    private fun loadDetailedTvShow(show: TvShow) {
        loadDetailedTvShowJob?.cancel()
        loadDetailedTvShowJob = viewModelScope.launch(Dispatchers.IO) {
            getShowDetails(show)
                .onSuccess { detailedTvShow ->
                    _state.value = _state.value.copy(
                        //data = _state.value.data.map { if (it.id == show.id) detailedTvShow else it }
                        detailedTvShow = detailedTvShow,
                        metaData = _state.value.metaData.copy(detailedDataState = DataState.Success)
                    )
                }
                .onFailure {
                    _state.value = _state.value.copy(
                        metaData = MetaData(
                            detailedDataState = DataState.Failed,
                            message = it.message ?: "Something went wrong"
                        )
                    )
                }
        }
    }

    private fun loadSimilarTvShows(show: TvShow) {
        loadSimilarTvShowsJob?.cancel()
        loadSimilarTvShowsJob = viewModelScope.launch(Dispatchers.IO) {
            getSimilarShows(show)
                .onSuccess { similarTvShows ->
                    _state.value = _state.value.copy(
                        similarTvShows = similarTvShows,
                        metaData = _state.value.metaData.copy(similarTvShowsDataState = DataState.Success)
                    )
                }
                .onFailure {
                    //fixme: what if both loadDetailedTvShow and loadSimilarTvShows fail? there's only a single message
                    _state.value.metaData = _state.value.metaData.copy(
                        similarTvShowsDataState = DataState.Failed,
                        message = it.message ?: "Something went wrong"
                    )
                }
        }
    }

    private fun loadThisWeekTrendingTvShows() {
        _state.value = _state.value.copy(
            metaData = MetaData(dataState = DataState.Loading)
        )
        viewModelScope.launch(Dispatchers.IO) {
            getThisWeekTrendingShows()
                .onSuccess { tvShows ->
                    _state.value = _state.value.copy(
                        data = tvShows,
                        metaData = MetaData(dataState = DataState.Success)
                    )
                }
                .onFailure {
                    _state.value = _state.value.copy(
                        metaData = MetaData(
                            dataState = DataState.Failed,
                            message = it.message ?: "Something went wrong"
                        )
                    )
                }
        }
    }

}
