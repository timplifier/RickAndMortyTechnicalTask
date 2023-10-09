package com.timplifier.data.repositories

import com.timplifier.data.base.makePagingRequest
import com.timplifier.data.base.makeRemoteRequest
import com.timplifier.data.local.db.daos.CharacterDao
import com.timplifier.data.remote.apiservices.CharacterApiService
import com.timplifier.data.remote.pagingSources.CharactersPagingSource
import com.timplifier.domain.models.CharacterModel
import com.timplifier.domain.repositories.CharacterRepository
import io.reactivex.Observable
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterApiService: CharacterApiService,
    private val characterDao: CharacterDao
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
                characterDao,
                name,
                status,
                species,
                gender
            )
        )

    override fun fetchSingleCharacter(id: Int) = makeRemoteRequest {
        characterApiService.fetchSingleCharacter(id).map { it.toDomain() }
    }

    override fun getLocalCharacters(
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ): Observable<List<CharacterModel>> =
        characterDao.getCharacters(name, status, species, gender)
            .map { charactersDto -> charactersDto.map { it.toDomain() } }

    override fun getLocalSingleCharacter(id: Int): Observable<CharacterModel> =
        characterDao.getSingleCharacter(id).map { it.toDomain() }
}