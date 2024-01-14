package com.harsh.samples.thisweektvshow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TvShowEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val voteAvg: Float,
    val isFavorite: Boolean
)
