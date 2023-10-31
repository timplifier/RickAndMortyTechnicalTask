package com.timplifier.data.repositories

import com.timplifier.data.base.makeNetworkRequest
import com.timplifier.data.base.makePagingRequest
import com.timplifier.data.local.db.realms.CharacterRealm
import com.timplifier.data.remote.apiservices.CharacterApiService
import com.timplifier.data.remote.pagingSources.CharactersPagingSource
import com.timplifier.domain.repositories.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterApiService: CharacterApiService,
    private val characterRealm: CharacterRealm,
) : CharacterRepository {

    override fun fetchCharacters(
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ) =
        makePagingRequest(
            CharactersPagingSource(
                characterApiService,
                characterRealm,
                name,
                status,
                species,
                gender
            )
        )

    override fun fetchSingleCharacter(id: Int) = makeNetworkRequest {
        characterApiService.fetchSingleCharacter(id)
    }

    override fun getLocalCharacters(
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ) =
        characterRealm.queryCharacters(name, status, species, gender)

    override fun getLocalSingleCharacter(id: Int) =
        characterRealm.queryCharacter(id)
}