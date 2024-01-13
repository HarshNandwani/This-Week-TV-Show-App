package com.harsh.samples.thisweektvshow.domain.model

data class TvShow(
    val id: Long,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val voteAvg: Float,
    val genres: List<String> = emptyList(),
    val seasons: List<TvShowSeason> = emptyList(),
    val isFavorite: Boolean = false
)
