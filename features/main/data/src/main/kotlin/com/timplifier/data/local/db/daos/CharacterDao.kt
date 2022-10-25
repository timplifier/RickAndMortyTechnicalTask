package com.timplifier.data.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.timplifier.data.remote.dtos.CharacterDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(vararg characters: CharacterDto)

    @Query("SELECT * FROM characterdto")
    fun getCharacters(): Flow<List<CharacterDto>>

    @Query("SELECT * FROM characterdto WHERE id =:id")
    fun getSingleCharacter(id: Int): Flow<CharacterDto>
}