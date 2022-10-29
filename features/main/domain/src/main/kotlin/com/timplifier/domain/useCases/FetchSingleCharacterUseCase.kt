package com.timplifier.domain.useCases

import com.timplifier.domain.repositories.CharacterRepository
import javax.inject.Inject

class FetchSingleCharacterUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    operator fun invoke(id: Int) = characterRepository.fetchSingleCharacter(id)
}