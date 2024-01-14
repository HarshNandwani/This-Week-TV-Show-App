package com.harsh.samples.thisweektvshow.presentation

import com.harsh.samples.thisweektvshow.domain.model.Data
import com.harsh.samples.thisweektvshow.domain.model.Source
import com.harsh.samples.thisweektvshow.domain.model.TvShow

data class UiState(
    var metaData: MetaData = MetaData(),
    val displayTvShows: List<TvShow> = emptyList(),
    val trendingShowsData: Data = Data(emptyList(), Source.NONE,""),
    val searchedTvShows: List<TvShow> = emptyList(),
    val detailedTvShow: TvShow? = null,
    val previousDetailTvShows: ArrayDeque<TvShow> = ArrayDeque(),
    val similarTvShows: List<TvShow> = emptyList(),
    var searchText: String = ""
)

data class MetaData(
    val message: String? = null,
    val dataState: DataState = DataState.NotRequested,
    val searchedTvShowDataState: DataState = DataState.NotRequested,
    val detailedDataState: DataState = DataState.NotRequested,
    val similarTvShowsDataState: DataState = DataState.NotRequested
)

enum class DataState { NotRequested, Loading, Success, Failed }
