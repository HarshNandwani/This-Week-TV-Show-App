package com.harsh.samples.thisweektvshow.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.harsh.samples.thisweektvshow.domain.repository.FakeTvShowRepository
import com.harsh.samples.thisweektvshow.domain.repository.TvShowRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Test

class GetSearchedShowsUseCaseTest {

    private val tvShowRepository: TvShowRepository = FakeTvShowRepository()
    private val sut = GetSearchedShowsUseCase(tvShowRepository)

    @Test
    fun `WHEN searchQuery parameter is blank THEN throw IllegalArgumentException`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            runTest { sut("") }
        }
        assertThat(exception).hasMessageThat().matches("Search query should not be empty")
    }

    @Test
    fun `WHEN searchQuery is not blank THEN call getSearchedTvShows repository method`() = runTest {
        val sampleQuery = "second"
        val actualResult = sut(sampleQuery)
        val expected = tvShowRepository.getSearchedTvShows(sampleQuery)

        assertThat(actualResult).isEqualTo(expected)
    }

}