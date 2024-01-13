package com.harsh.samples.thisweektvshow.domain.repository

import com.harsh.samples.thisweektvshow.domain.model.Result
import com.harsh.samples.thisweektvshow.domain.model.TvShow

interface TvShowRepository {
    suspend fun getTrendingThisWeek(): Result<List<TvShow>>
    suspend fun getSearchedTvShows(query: String): Result<List<TvShow>>
    suspend fun getSimilarTvShows(similarToShowId: Long): Result<List<TvShow>>
}
