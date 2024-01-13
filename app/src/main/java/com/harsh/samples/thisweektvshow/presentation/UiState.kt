package com.harsh.samples.thisweektvshow.presentation

import com.harsh.samples.thisweektvshow.domain.model.TvShow

data class UiState(
    var metaData: MetaData = MetaData(),
    val data: List<TvShow> = emptyList(),
    val detailedTvShow: TvShow? = null,
    val similarTvShows: List<TvShow> = emptyList()
)

data class MetaData(
    val message: String? = null,
    val dataState: DataState = DataState.NotRequested,
    val detailedDataState: DataState = DataState.NotRequested,
    val similarTvShowsDataState: DataState = DataState.NotRequested
)

enum class DataState { NotRequested, Loading, Success, Failed }
