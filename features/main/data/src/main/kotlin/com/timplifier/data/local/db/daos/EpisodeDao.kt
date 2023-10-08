package com.timplifier.data.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.timplifier.data.remote.dtos.EpisodeDto
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEpisodes(vararg episodes: EpisodeDto)

    @Query("SELECT * FROM episodedto WHERE url =:url")
    fun getSingleEpisode(url: String): Observable<EpisodeDto>
}