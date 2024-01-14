package com.harsh.samples.thisweektvshow.domain.repository

import com.harsh.samples.thisweektvshow.domain.model.Data
import com.harsh.samples.thisweektvshow.domain.model.Result
import com.harsh.samples.thisweektvshow.domain.model.TvShow

interface TvShowRepository {
    /*
    * Loads this weeks trending TV shows
    * */
    suspend fun getTrendingThisWeek(): Result<Data>

    /*
    * Would be used to load TV Show genres & seasons details.
    *
    * [Implementation details]: Could have used a separate "MinimalTvShow" model but is counter productive in this case.
    * because we get some details like overview in trending api itself but some details like genres & seasons in detail api
    * so passing and returning same type, but the return is with more details!
    * */
    suspend fun getTvShowDetails(tvShow: TvShow): Result<TvShow>

    /*
    * Loads TV Shows matching query parameter
    * */
    suspend fun getSearchedTvShows(query: String): Result<List<TvShow>>

    /*
    * Loads TV shows similar to the provided show id
    * */
    suspend fun getSimilarTvShows(similarToShowId: Long): Result<List<TvShow>>

    /*
    * Mark a tv show as favorite
    * */
    suspend fun toggleFavorite(tvShowId: Long, isFavorite: Boolean)
}
