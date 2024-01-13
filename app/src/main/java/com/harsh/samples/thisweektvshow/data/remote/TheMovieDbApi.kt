package com.harsh.samples.thisweektvshow.data.remote

import com.harsh.samples.thisweektvshow.data.remote.dto.DetailedTvShowDto
import com.harsh.samples.thisweektvshow.data.remote.dto.TvShowsResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbApi {
    @GET("/3/trending/tv/week")
    fun getTrendingShowsThisWeek(): Response<TvShowsResponseDto>

    @GET("/3/tv/{id}")
    fun getTvShowDetails(@Path("id") showId: Long): Response<DetailedTvShowDto>

    @GET("/3/search/tv")
    fun getSearchedTvShows(@Query("query") query: String): Response<TvShowsResponseDto>

    @GET("/3/tv/{id}/similar")
    fun getSimilarTvShows(@Path("id") showId: Long): Response<TvShowsResponseDto>
}
