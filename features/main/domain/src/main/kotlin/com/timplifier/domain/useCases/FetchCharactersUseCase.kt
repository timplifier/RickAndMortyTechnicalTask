package com.timplifier.domain.useCases

import com.timplifier.domain.repositories.CharacterRepository
import javax.inject.Inject

class FetchCharactersUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    operator fun invoke(name: String?, status: String?, species: String?, gender: String?) =
        characterRepository.fetchCharacters(name, status, species, gender)
}