package com.timplifier.data.repositories

import com.timplifier.data.base.makePagingRequest
import com.timplifier.data.remote.apiservices.CharactersApiService
import com.timplifier.data.remote.pagingSources.CharactersPagingSource
import com.timplifier.domain.repository.CharactersRepository
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val charactersApiService: CharactersApiService
) : CharactersRepository {
    override fun fetchCharacters() = makePagingRequest(CharactersPagingSource(charactersApiService))
}