package com.harsh.samples.thisweektvshow.data.repository

import com.harsh.samples.thisweektvshow.data.ConnectivityDataSource
import com.harsh.samples.thisweektvshow.data.local.dao.TvShowDao
import com.harsh.samples.thisweektvshow.data.remote.TheMovieDbApi
import com.harsh.samples.thisweektvshow.domain.model.Data
import com.harsh.samples.thisweektvshow.domain.model.Result
import com.harsh.samples.thisweektvshow.domain.model.Source
import com.harsh.samples.thisweektvshow.domain.model.TvShow
import com.harsh.samples.thisweektvshow.domain.model.TvShowLoadException
import com.harsh.samples.thisweektvshow.domain.repository.TvShowRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response

/*
* Used repository - datasource pattern
* This class contains only data logic, no business logic allowed!
*
* [Implementation Details] I have made this repository to be single source of truth - it might take little longer to
* load data but the returned data would be inclusive of everything the ui/presentation might need.
* ex: Repository takes case of inferring previously added favorites in local and apply them to new data from remote.
*
* For faster load times we can implement certain logic in ViewModel.
* ex: ViewModel loads remote data first and displays it ASAP, then loads local data to later show favorites in UI
*
* I have chosen the first approach for several reasons:
* - Dealing with relatively low amount of data.
* - load time from API is fast and given low amount of data and less columns the room data loading is also quicker.
* - its a sample app, won't be used by end user also this implementation is a bit easier.
* */
class DefaultTvShowRepository(
    private val connectivityDataSource: ConnectivityDataSource,
    private val localDataSource: TvShowDao,
    private val remoteDataSource: TheMovieDbApi,
    private val extCoroutineScope: CoroutineScope
) : TvShowRepository {

    /*
    * Loads trending tv shows with basic caching
    * Checks for internet connection
    *  - if NOT connected
    *       THEN Loads data from local source
    *  - else (connected)
    *       THEN load data from both. Remote data for latest trending and local data to see if favorites are added
    *
    * Proper handling in each failure cases!
    *
    * [Implementation Details] used a very basic caching strategy for the purpose of assignment.
    * For real apps / If there's more time - a more sophisticated caching mechanism can be implemented
    * for ex: If cache is older than x days/requests we could delete it to keep load times faster!
    * */
    override suspend fun getTrendingThisWeek(): Result<Data> = coroutineScope {
        val remoteDataResult: Result<List<TvShow>>
        val localDataResult: Result<List<TvShow>>

        val localDataJob = async { loadShowsLocal() }
        if (!connectivityDataSource.isConnected()) {
            localDataResult = localDataJob.await()
            when (localDataResult) {
                is Result.Success -> {
                    Result.Success(Data(localDataResult.data, Source.LOCAL, "Data loaded from cache - no connection"))
                }
                is Result.Failure -> {
                    Result.Failure(TvShowLoadException("No cache data and no internet connection"))
                }
            }
        } else {
            val remoteDataJob = async { loadTrendingShowsRemote() }
            remoteDataResult = remoteDataJob.await()
            localDataResult = localDataJob.await()

            val localTvShows = when (localDataResult) {
                is Result.Success -> { localDataResult.data }
                is Result.Failure -> { null }
            }

            when (remoteDataResult) {
                is Result.Success -> {
                    val updatedTvShowsWithFavorites = inferFavoritesIfAny(localTvShows, remoteDataResult.data)
                    // add these results to local db for caching
                    extCoroutineScope.launch(extCoroutineScope.coroutineContext) {
                        remoteDataResult.data.forEach { localDataSource.addTvShow(it.toEntity()) }
                    }
                    Result.Success(Data(updatedTvShowsWithFavorites, Source.REMOTE, "Successful load"))
                }
                is Result.Failure -> {
                    localTvShows?.let {
                        Result.Success(Data(it, Source.LOCAL, "Data loaded from cache - Remote data load failed"))
                    } ?: Result.Failure(TvShowLoadException("No cache data and remote data load failed"))
                }
            }
        }
    }

    override suspend fun getTvShowDetails(tvShow: TvShow): Result<TvShow> {
        if (!connectivityDataSource.isConnected())
            return Result.Failure(TvShowLoadException("Not connected to internet"))
        val response = try {
            remoteDataSource.getTvShowDetails(tvShow.id)
        } catch (e: Exception) {
            return Result.Failure(e)
        }

        return if (response.isSuccessful) {
            val detailedTvShowDto = response.body() ?: return Result.Failure(Exception(response.exceptionMessage()))
            val detailedTvShow = detailedTvShowDto.toDomain()
            // Check if this is added as favorite
            if (localDataSource.get(detailedTvShow.id)?.isFavorite == true) detailedTvShow.isFavorite = true
            Result.Success(detailedTvShow)
        } else {
            Result.Failure(TvShowLoadException(response.exceptionMessage()))
        }
    }

    override suspend fun getSearchedTvShows(query: String): Result<List<TvShow>> {
        if (!connectivityDataSource.isConnected())
            return Result.Failure(TvShowLoadException("Cannot load search data - No internet connection"))
        val response = try {
            remoteDataSource.getSearchedTvShows(query)
        } catch (e: Exception) {
            return Result.Failure(e)
        }

        return if (response.isSuccessful) {
            val searchedTvShows = response.body()?.results ?: return Result.Failure(
                TvShowLoadException(response.exceptionMessage())
            )
            Result.Success(searchedTvShows.map { it.toDomain() })
        } else {
            Result.Failure(TvShowLoadException(response.exceptionMessage()))
        }
    }

    override suspend fun getSimilarTvShows(similarToShowId: Long): Result<List<TvShow>> {
        if (!connectivityDataSource.isConnected())
            return Result.Failure(TvShowLoadException("Not connected to internet"))
        val response = try {
            remoteDataSource.getSimilarTvShows(similarToShowId)
        } catch (e: Exception) {
            return Result.Failure(e)
        }

        return if (response.isSuccessful) {
            val tvShowsDto =
                response.body()?.results
                    ?: return Result.Failure(Exception(response.exceptionMessage()))
            val tvShows = tvShowsDto.map { it.toDomain() }
            Result.Success(tvShows)
        } else {
            Result.Failure(TvShowLoadException(response.exceptionMessage()))
        }
    }

    override suspend fun toggleFavorite(tvShowId: Long, isFavorite: Boolean) {
        localDataSource.markFavorite(tvShowId, isFavorite)
    }

    // Utility functions

    private fun <T> Response<T>.exceptionMessage(): String =
        "${this.code()} - ${this.errorBody()?.string() ?: this.message()}"

    private suspend fun loadTrendingShowsRemote(): Result<List<TvShow>> {
        val response = try {
            remoteDataSource.getTrendingShowsThisWeek()
        } catch (e: Exception) {
            return Result.Failure(e)
        }

        return if (response.isSuccessful) {
            val tvShowsDto =
                response.body()?.results ?: return Result.Failure(Exception(response.exceptionMessage()))
            val tvShows = tvShowsDto.map { it.toDomain() }
            Result.Success(tvShows)
        } else {
            Result.Failure(TvShowLoadException(response.exceptionMessage()))
        }
    }

    private suspend fun loadShowsLocal(): Result<List<TvShow>> {
        val tvShowEntities = localDataSource.getAll()
        return if (tvShowEntities.isEmpty()) {
            Result.Failure(TvShowLoadException("No data"))
        } else {
            Result.Success(tvShowEntities.map { it.toDomain() })
        }
    }

    //As favorites are persisted locally, here we take local and remote list and updated of shows are favorite in remote list
    private fun inferFavoritesIfAny(localList: List<TvShow>?, remoteList: List<TvShow>): List<TvShow> {
        val resultantList by lazy { remoteList.toMutableList() }
        localList?.forEach { localTvShow ->
            if (localTvShow.isFavorite) {
                resultantList.find { remoteTvShow -> localTvShow.id == remoteTvShow.id }?.isFavorite = true
            }
        } ?: return remoteList
        return resultantList
    }
}
