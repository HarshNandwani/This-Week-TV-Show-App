package com.harsh.samples.thisweektvshow.domain.repository

import com.harsh.samples.thisweektvshow.domain.model.Data
import com.harsh.samples.thisweektvshow.domain.model.Result
import com.harsh.samples.thisweektvshow.domain.model.Source
import com.harsh.samples.thisweektvshow.domain.model.TvShow
import com.harsh.samples.thisweektvshow.domain.model.TvShowSeason

class FakeTvShowRepository : TvShowRepository {

    private val tvShowList = listOf(
        TvShow(1, "First Show", "Xyz xyz xyz", "example.com/xyz1.jpg", "", 8.1f),
        TvShow(2, "Second Show", "Xyz xyz xyz", "example.com/xyz2.jpg","",8.2f),
        TvShow(3, "Third Show", "Xyz xyz xyz", "example.com/xyz3.jpg", "",8.3f),
        TvShow(4, "Forth Show", "Xyz xyz xyz", "example.com/xyz4.jpg", "",8.4f),
    )

    override suspend fun getTrendingThisWeek(): Result<Data> {
        return Result.Success(Data(tvShowList, Source.REMOTE, "Successful load"))
    }

    override suspend fun getTvShowDetails(tvShow: TvShow): Result<TvShow> {
        return Result.Success(
            tvShow.copy(
                genres = listOf("Crime", "Drama"),
                seasons = listOf(
                    TvShowSeason(1, "Season 1", 10, 1, 9.6f),
                    TvShowSeason(2, "Season 2", 13, 2, 9.9f)
                )
            )
        )
    }

    override suspend fun getSearchedTvShows(query: String): Result<List<TvShow>> {
        return Result.Success(tvShowList.filter { it.title.lowercase().contains(query.lowercase()) })
    }

    override suspend fun getSimilarTvShows(similarToShowId: Long): Result<List<TvShow>> {
        // sample failure and success
        return if (similarToShowId < 0) {
            Result.Failure(IllegalArgumentException("Invalid ID"))
        } else {
            Result.Success(tvShowList)
        }

    }
}
