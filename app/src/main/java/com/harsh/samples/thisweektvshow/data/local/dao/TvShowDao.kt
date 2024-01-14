package com.harsh.samples.thisweektvshow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.harsh.samples.thisweektvshow.data.local.entity.TvShowEntity

@Dao
interface TvShowDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTvShow(tvShowEntity: TvShowEntity)

    @Query("SELECT * FROM TvShowEntity")
    suspend fun getAll(): List<TvShowEntity>

    @Query("SELECT * FROM TvShowEntity WHERE id = :id")
    suspend fun get(id: Long): TvShowEntity?

    @Update
    suspend fun update(tvShowEntity: TvShowEntity)

    @Query("UPDATE TVSHOWENTITY SET isFavorite = :isFavorite WHERE id = :tvShowId")
    suspend fun markFavorite(tvShowId: Long, isFavorite: Boolean)


    @Delete
    suspend fun delete(tvShowEntity: TvShowEntity)
}
