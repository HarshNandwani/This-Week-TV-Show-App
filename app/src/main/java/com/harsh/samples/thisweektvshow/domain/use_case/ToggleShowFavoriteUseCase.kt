package com.harsh.samples.thisweektvshow.domain.use_case

import com.harsh.samples.thisweektvshow.domain.repository.TvShowRepository

class ToggleShowFavoriteUseCase(private val repository: TvShowRepository) {
    suspend operator fun invoke(tvShowId: Long, isFavorite: Boolean) {
        repository.toggleFavorite(tvShowId, isFavorite)
    }
}
