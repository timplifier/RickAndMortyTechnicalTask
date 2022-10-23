package com.timplifier.data.remote.pagingSources

import com.timplifier.data.base.BasePagingSource
import com.timplifier.data.remote.apiservices.CharactersApiService
import com.timplifier.data.remote.dtos.CharacterDto
import com.timplifier.domain.models.CharactersModel

class CharactersPagingSource(
    private val charactersApiService: CharactersApiService
) :
    BasePagingSource<CharacterDto, CharactersModel>({
        charactersApiService.fetchCharacters(it)
    })