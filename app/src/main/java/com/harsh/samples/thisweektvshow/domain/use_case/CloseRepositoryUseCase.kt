package com.harsh.samples.thisweektvshow.domain.use_case

import com.harsh.samples.thisweektvshow.domain.repository.TvShowRepository

class CloseRepositoryUseCase(private val repository: TvShowRepository) {
    suspend operator fun invoke() {
        repository.closeRepository()
    }
}
