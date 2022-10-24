package com.timplifier.domain.repositories

import androidx.paging.PagingData
import com.timplifier.domain.models.CharacterModel
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun fetchCharacters(): Flow<PagingData<CharacterModel>>
}