package com.harsh.samples.thisweektvshow.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.harsh.samples.thisweektvshow.data.local.TvShowDatabase
import com.harsh.samples.thisweektvshow.data.local.dao.DummyTvShows.gameOfThrones
import com.harsh.samples.thisweektvshow.data.local.dao.DummyTvShows.houseOfTheDragon
import com.harsh.samples.thisweektvshow.data.local.dao.DummyTvShows.moneyHeist
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TvShowDaoTests {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: TvShowDatabase
    private lateinit var sut: TvShowDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TvShowDatabase::class.java
        ).allowMainThreadQueries().build()
        sut = database.tvShowDao
    }

    @After
    fun closeDown() {
        database.close()
    }

    @Test
    fun addAndGetTvShowTest() = runTest {
        sut.addTvShow(gameOfThrones)
        val tvShow = sut.get(gameOfThrones.id)

        assertThat(tvShow).isEqualTo(gameOfThrones)
    }

    @Test
    fun addAndGetAllTvShowsTest() = runTest {
        sut.addTvShow(gameOfThrones)
        sut.addTvShow(houseOfTheDragon)
        sut.addTvShow(moneyHeist)

        val allTvShows = sut.getAll()

        assertThat(allTvShows).containsExactly(gameOfThrones, houseOfTheDragon, moneyHeist)
    }

    @Test
    fun addAndGetTrendingTvShowsTest() = runTest {
        sut.addTvShow(gameOfThrones)
        sut.addTvShow(houseOfTheDragon)
        sut.addTvShow(moneyHeist)

        val trendingTvShows = sut.getTrendingTvShows()

        assertThat(trendingTvShows).containsExactly(gameOfThrones, houseOfTheDragon)
    }

    @Test
    fun addAndGetFavoriteTvShowsTest() = runTest {
        sut.addTvShow(gameOfThrones)
        sut.addTvShow(houseOfTheDragon)
        sut.addTvShow(moneyHeist)

        val trendingTvShows = sut.getFavoriteTvShows()

        assertThat(trendingTvShows).containsExactly(gameOfThrones)
    }

    @Test
    fun updateTvShowTest() = runTest {
        sut.addTvShow(gameOfThrones)
        val updatedGOT = gameOfThrones.copy(
            overview = "New overview",
            voteAvg = 9.8f
        )
        sut.update(updatedGOT)

        val updatedTvShow = sut.get(gameOfThrones.id)

        assertThat(updatedTvShow).isEqualTo(updatedGOT)
    }

    @Test
    fun markFavoriteTest() = runTest {
        sut.addTvShow(gameOfThrones)
        sut.addTvShow(houseOfTheDragon)
        sut.addTvShow(moneyHeist)

        sut.markFavorite(houseOfTheDragon.id, true)
        sut.markFavorite(gameOfThrones.id, false)

        val favoriteTvShows = sut.getFavoriteTvShows()
        println("FAV:: $favoriteTvShows")
        assertThat(favoriteTvShows).containsExactly(houseOfTheDragon.copy(isFavorite = true))
    }

    @Test
    fun deleteTest() = runTest {
        sut.addTvShow(gameOfThrones)
        sut.addTvShow(houseOfTheDragon)
        sut.addTvShow(moneyHeist)

        sut.delete(moneyHeist)

        val tvShow = sut.get(moneyHeist.id)
        val allShows = sut.getAll()

        assertThat(tvShow).isNull()
        assertThat(allShows).containsExactly(gameOfThrones, houseOfTheDragon)
    }
}
