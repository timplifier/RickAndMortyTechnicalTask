package com.timplifier.data.remote.pagingSources

import com.timplifier.data.base.BasePagingSource
import com.timplifier.data.remote.apiservices.CharacterApiService
import com.timplifier.data.remote.dtos.CharacterDto
import com.timplifier.domain.models.CharacterModel

class CharactersPagingSource(
    private val characterApiService: CharacterApiService
) :
    BasePagingSource<CharacterDto, CharacterModel>({
        characterApiService.fetchCharacters(it)
    })