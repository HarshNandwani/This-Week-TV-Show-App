package com.harsh.samples.thisweektvshow.domain.repository

import com.harsh.samples.thisweektvshow.domain.model.Result
import com.harsh.samples.thisweektvshow.domain.model.TvShow

interface TvShowRepository {
    /*
    * Loads this weeks trending TV shows
    * */
    suspend fun getTrendingThisWeek(): Result<List<TvShow>>

    /*
    * Would be used to load TV Show genres & seasons details.
    *
    * [Implementation details]: Could have used a separate "MinimalTvShow" model but is counter productive in this case.
    * because we get some details like overview in trending api itself but some details like genres & seasons in detail api
    * so passing and returning same type, but the return is with more details!
    * */
    suspend fun getTvShowDetails(tvShow: TvShow): TvShow

    /*
    * Loads TV Shows matching query parameter
    * */
    suspend fun getSearchedTvShows(query: String): Result<List<TvShow>>

    /*
    * Loads TV shows similar to the provided show id
    * */
    suspend fun getSimilarTvShows(similarToShowId: Long): Result<List<TvShow>>
}
