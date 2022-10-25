package com.timplifier.domain.useCases

import com.timplifier.domain.repositories.CharacterRepository
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    operator fun invoke() = characterRepository.getLocalCharacters()
}