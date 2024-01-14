package com.harsh.samples.thisweektvshow.domain.use_case

import com.harsh.samples.thisweektvshow.domain.model.Data
import com.harsh.samples.thisweektvshow.domain.model.Result
import com.harsh.samples.thisweektvshow.domain.repository.TvShowRepository

class GetThisWeekTrendingShowsUseCase(private val repository: TvShowRepository) {
    suspend operator fun invoke(): Result<Data> {
        return repository.getTrendingThisWeek()
    }
}
