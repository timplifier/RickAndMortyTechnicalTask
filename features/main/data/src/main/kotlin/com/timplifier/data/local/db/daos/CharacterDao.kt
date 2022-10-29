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

    @Query(
        """ SELECT * FROM characterdto WHERE 
(:name IS NULL OR name LIKE '%' || :name || '%') AND
(:status IS NULL OR status = :status) AND
(:gender IS NULL OR gender = :gender) AND 
(:species IS NULL OR species = :species)"""
    )
    fun getCharacters(
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ): Flow<List<CharacterDto>>

    @Query("SELECT * FROM characterdto WHERE id =:id")
    fun getSingleCharacter(id: Int): Flow<CharacterDto>
}