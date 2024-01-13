package com.harsh.samples.thisweektvshow.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.samples.thisweektvshow.domain.use_case.GetThisWeekTrendingShowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowViewModel @Inject constructor(
    private val getThisWeekTrendingShows: GetThisWeekTrendingShowsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    init {
        loadThisWeekTrendingTvShows()
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
