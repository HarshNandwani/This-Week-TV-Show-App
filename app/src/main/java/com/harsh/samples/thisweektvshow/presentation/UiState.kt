package com.harsh.samples.thisweektvshow.presentation

import com.harsh.samples.thisweektvshow.domain.model.TvShow

data class UiState(
    val metaData: MetaData = MetaData(),
    val data: List<TvShow> = emptyList()
)

data class MetaData(
    val dataState: DataState = DataState.NotRequested,
    val message: String? = null,
    val detailedDataState: DataState = DataState.NotRequested // used when loading details of tv show
)

enum class DataState { NotRequested, Loading, Success, Failed }
