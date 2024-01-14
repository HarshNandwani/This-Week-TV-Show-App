package com.harsh.samples.thisweektvshow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.harsh.samples.thisweektvshow.data.local.dao.TvShowDao
import com.harsh.samples.thisweektvshow.data.local.entity.TvShowEntity

@Database(
    entities = [TvShowEntity::class],
    version = 1
)
abstract class TvShowDatabase : RoomDatabase() {
    abstract val tvShowDao: TvShowDao

    companion object {
        const val DATABASE_NAME = "tv_show_db"
    }

}
