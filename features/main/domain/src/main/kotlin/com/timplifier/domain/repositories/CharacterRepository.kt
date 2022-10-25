package com.timplifier.domain.repositories

import androidx.paging.PagingData
import com.timplifier.common.either.Either
import com.timplifier.domain.models.CharacterModel
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun fetchCharacters(): Flow<PagingData<CharacterModel>>
    fun fetchSingleCharacter(id: Int): Flow<Either<String, CharacterModel>>
    fun getLocalCharacters(): Flow<List<CharacterModel>>
    fun getLocalSingleCharacter(id: Int): Flow<CharacterModel>
}