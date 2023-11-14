package com.timplifier.domain.repositories

import androidx.paging.PagingData
import com.timplifier.common.either.Either
import com.timplifier.domain.models.CharacterModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockCharacterRepository(private val characters: List<CharacterModel>) : CharacterRepository {

    override fun fetchCharacters(
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ): Flow<PagingData<CharacterModel>> {
        return flow {
            val filteredCharacters = characters.filter { character ->
                (name == null || character.name.contains(name, ignoreCase = true)) &&
                        (status == null || character.status.equals(status, ignoreCase = true)) &&
                        (species == null || character.species.equals(species, ignoreCase = true)) &&
                        (gender == null || character.gender.equals(gender, ignoreCase = true))
            }
            emit(PagingData.from(filteredCharacters))
        }
    }

    override fun fetchSingleCharacter(id: Int): Flow<Either<String, CharacterModel>> {
        return flow {
            val fetchedCharacter = characters.find { it.id == id }
            emit(if (fetchedCharacter != null) Either.Right(fetchedCharacter) else Either.Left("Character with provided is not found"))
        }
    }

    override fun getLocalCharacters(
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ): Flow<List<CharacterModel>> {
        return flow {
            emit(characters.filter { character ->
                (name == null || character.name.contains(name, ignoreCase = true)) &&
                        (status == null || character.status.equals(status, ignoreCase = true)) &&
                        (species == null || character.species.equals(species, ignoreCase = true)) &&
                        (gender == null || character.gender.equals(gender, ignoreCase = true))
            })
        }
    }

    override fun getLocalSingleCharacter(id: Int): Flow<CharacterModel> {
        return flow { emit(characters.find { it.id == id }!!) }
    }
}