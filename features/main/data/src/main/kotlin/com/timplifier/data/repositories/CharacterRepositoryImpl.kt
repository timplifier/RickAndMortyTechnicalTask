package com.timplifier.data.repositories

import com.timplifier.data.base.makePagingRequest
import com.timplifier.data.remote.apiservices.CharacterApiService
import com.timplifier.data.remote.pagingSources.CharactersPagingSource
import com.timplifier.domain.repositories.CharactersRepository
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val characterApiService: CharacterApiService
) : CharactersRepository {
    override fun fetchCharacters() = makePagingRequest(CharactersPagingSource(characterApiService))
}