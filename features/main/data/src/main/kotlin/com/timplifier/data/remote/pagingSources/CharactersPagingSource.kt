package com.timplifier.data.remote.pagingSources

import com.timplifier.data.base.BasePagingSource
import com.timplifier.data.local.db.realms.CharacterRealm
import com.timplifier.data.remote.apiservices.CharacterApiService
import com.timplifier.data.remote.dtos.CharacterDto
import com.timplifier.data.remote.dtos.toRealm
import com.timplifier.domain.models.CharacterModel

class CharactersPagingSource(
    private val characterApiService: CharacterApiService,
    private val characterRealm: CharacterRealm,
    private val name: String?,
    private val status: String?,
    private val species: String?,
    private val gender: String?
) :
    BasePagingSource<CharacterDto, CharacterModel>({
        characterApiService.fetchCharacters(it, name, status, species, gender).also { characters ->
            characterRealm.writeCharacters(characters.results.map { it.toRealm() })
        }
    })