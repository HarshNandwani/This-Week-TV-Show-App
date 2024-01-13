package com.harsh.samples.thisweektvshow.domain.use_case

import com.harsh.samples.thisweektvshow.domain.model.TvShow
import com.harsh.samples.thisweektvshow.domain.repository.TvShowRepository

class GetShowDetailsUseCase(private val repository: TvShowRepository) {
    suspend operator fun invoke(show: TvShow): TvShow {
        return repository.getTvShowDetails(show)
    }
}
