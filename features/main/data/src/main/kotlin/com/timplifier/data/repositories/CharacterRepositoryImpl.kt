package com.timplifier.data.repositories

import com.timplifier.data.base.makePagingRequest
import com.timplifier.data.remote.apiservices.CharacterApiService
import com.timplifier.data.remote.pagingSources.CharactersPagingSource
import com.timplifier.domain.repositories.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterApiService: CharacterApiService
) : CharacterRepository {
    override fun fetchCharacters() = makePagingRequest(CharactersPagingSource(characterApiService))
}