package com.harsh.samples.thisweektvshow.domain.use_case

import com.harsh.samples.thisweektvshow.domain.model.Result
import com.harsh.samples.thisweektvshow.domain.model.TvShow
import com.harsh.samples.thisweektvshow.domain.repository.TvShowRepository

class GetSimilarShowsUseCase(private val repository: TvShowRepository) {
    suspend operator fun invoke(similarToShow: TvShow): Result<List<TvShow>> {
        return repository.getSimilarTvShows(similarToShow.id)
    }
}
