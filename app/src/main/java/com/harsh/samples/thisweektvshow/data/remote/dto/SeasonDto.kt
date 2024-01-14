package com.harsh.samples.thisweektvshow.data.remote.dto


import com.google.gson.annotations.SerializedName

data class SeasonDto(
    @SerializedName("air_date")
    val airDate: String,
    @SerializedName("episode_count")
    val episodeCount: Int,
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("season_number")
    val seasonNumber: Byte,
    @SerializedName("vote_average")
    val voteAverage: Float
)