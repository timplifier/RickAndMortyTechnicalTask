package com.timplifier.domain.useCases

import com.timplifier.domain.repository.CharactersRepository
import javax.inject.Inject

class FetchCharactersUseCase @Inject constructor(
    private val charactersRepository: CharactersRepository
) {
    operator fun invoke() = charactersRepository.fetchCharacters()
}