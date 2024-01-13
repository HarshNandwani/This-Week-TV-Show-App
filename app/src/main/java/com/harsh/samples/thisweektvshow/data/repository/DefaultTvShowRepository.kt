package com.harsh.samples.thisweektvshow.data.repository

import com.harsh.samples.thisweektvshow.data.remote.Constants.posterBaseUrl
import com.harsh.samples.thisweektvshow.data.remote.TheMovieDbApi
import com.harsh.samples.thisweektvshow.data.remote.dto.TvShowDto
import com.harsh.samples.thisweektvshow.domain.model.Result
import com.harsh.samples.thisweektvshow.domain.model.TvShow
import com.harsh.samples.thisweektvshow.domain.model.TvShowLoadException
import com.harsh.samples.thisweektvshow.domain.repository.TvShowRepository
import retrofit2.Response

class DefaultTvShowRepository(
    //TODO: Add local data source for favorites and offline support.
    private val remoteDataSource: TheMovieDbApi
) : TvShowRepository {

    override suspend fun getTrendingThisWeek(): Result<List<TvShow>> {
        val response = try {
            remoteDataSource.getTrendingShowsThisWeek()
        } catch (e: Exception) {
            return Result.Failure(e)
        }

        return if (response.isSuccessful) {
            val tvShowsDto =
                response.body()?.results ?: return Result.Failure(Exception(response.message()))
            val tvShows = tvShowsDto.map { it.toDomain() }
            Result.Success(tvShows)
        } else {
            Result.Failure(TvShowLoadException(response.exceptionMessage()))
        }
    }

    override suspend fun getTvShowDetails(tvShow: TvShow): Result<TvShow> {
        TODO("Not yet implemented")
    }

    override suspend fun getSearchedTvShows(query: String): Result<List<TvShow>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSimilarTvShows(similarToShowId: Long): Result<List<TvShow>> {
        TODO("Not yet implemented")
    }

    // Utility functions

    private fun <T> Response<T>.exceptionMessage(): String =
        "${this.code()} - ${this.errorBody()?.string() ?: this.message()}"

    private fun TvShowDto.toDomain(): TvShow = TvShow(
        this.id,
        this.name,
        this.overview,
        "$posterBaseUrl${this.posterPath}",
        this.voteAverage,
    )
}
