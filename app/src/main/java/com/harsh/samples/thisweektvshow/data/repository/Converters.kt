package com.harsh.samples.thisweektvshow.data.repository

import com.harsh.samples.thisweektvshow.data.local.entity.TvShowEntity
import com.harsh.samples.thisweektvshow.data.remote.Constants
import com.harsh.samples.thisweektvshow.data.remote.dto.DetailedTvShowDto
import com.harsh.samples.thisweektvshow.data.remote.dto.TvShowDto
import com.harsh.samples.thisweektvshow.domain.model.TvShow
import com.harsh.samples.thisweektvshow.domain.model.TvShowSeason

fun TvShowDto.toDomain(): TvShow = TvShow(
    this.id,
    this.name,
    this.overview,
    "${Constants.posterBaseUrl}${this.posterPath}",
    "${Constants.backdropBaseUrl}${this.backdropPath}",
    this.voteAverage,
)

fun DetailedTvShowDto.toDomain(): TvShow = TvShow(
    this.id,
    this.name,
    this.overview,
    "${Constants.posterBaseUrl}${this.posterPath}",
    "${Constants.backdropBaseUrl}${this.backdropPath}",
    this.voteAverage,
    this.genres.map { it.name },
    this.seasons.map {
        TvShowSeason(
            it.id,
            it.name,
            it.episodeCount,
            it.seasonNumber,
            it.voteAverage
        )
    }
)

fun TvShowEntity.toDomain(): TvShow = TvShow(
    this.id,
    this.title,
    this.overview,
    this.posterUrl,
    this.backdropUrl,
    this.voteAvg,
    isFavorite = this.isFavorite
)

fun TvShow.toEntity(isTrending: Boolean = false, trendingNumber: Int = -1) = TvShowEntity(
    this.id,
    this.title,
    this.overview,
    this.posterUrl,
    this.backdropUrl,
    this.voteAvg,
    false,
    isTrending,
    trendingNumber
)
