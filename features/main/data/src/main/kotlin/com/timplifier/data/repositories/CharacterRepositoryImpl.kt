package com.timplifier.data.repositories

import com.timplifier.data.base.makeNetworkRequest
import com.timplifier.data.base.makePagingRequest
import com.timplifier.data.local.db.daos.CharacterDao
import com.timplifier.data.remote.apiservices.CharacterApiService
import com.timplifier.data.remote.pagingSources.CharactersPagingSource
import com.timplifier.domain.repositories.CharacterRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterApiService: CharacterApiService,
    private val characterDao: CharacterDao
) : CharacterRepository {

    override fun fetchCharacters() =
        makePagingRequest(CharactersPagingSource(characterApiService, characterDao))

    override fun fetchSingleCharacter(id: Int) = makeNetworkRequest {
        characterApiService.fetchSingleCharacter(id).toDomain()
    }

    override fun getLocalCharacters() =
        characterDao.getCharacters().map { charactersDto -> charactersDto.map { it.toDomain() } }

    override fun getLocalSingleCharacter(id: Int) =
        characterDao.getSingleCharacter(id).map { it.toDomain() }
}