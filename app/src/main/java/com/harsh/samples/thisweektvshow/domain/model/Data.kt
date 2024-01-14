package com.harsh.samples.thisweektvshow.domain.model

data class Data(
    val tvShows: List<TvShow>,
    val loadedFrom: Source,
    val message: String
)

enum class Source { NONE, LOCAL, REMOTE }
