package com.harsh.samples.thisweektvshow.domain.model

data class TvShowSeason(
    val id: Long,
    val name: String,
    val noOfEpisodes: Byte,
    val seasonNumber: Byte,
    val voteAvg: Float
)
