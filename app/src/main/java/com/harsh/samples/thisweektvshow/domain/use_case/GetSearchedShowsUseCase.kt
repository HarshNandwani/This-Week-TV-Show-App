package com.harsh.samples.thisweektvshow.domain.use_case

import com.harsh.samples.thisweektvshow.domain.model.Result
import com.harsh.samples.thisweektvshow.domain.model.TvShow
import com.harsh.samples.thisweektvshow.domain.repository.TvShowRepository

class GetSearchedShowsUseCase(private val repository: TvShowRepository) {
    suspend operator fun invoke(searchQuery: String): Result<List<TvShow>> {
        if (searchQuery.isBlank())
            throw IllegalArgumentException("Search query should not be empty")

        return repository.getSearchedTvShows(searchQuery)
    }
}
