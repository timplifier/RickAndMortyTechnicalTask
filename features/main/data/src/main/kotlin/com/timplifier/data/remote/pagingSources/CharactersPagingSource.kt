package com.timplifier.data.remote.pagingSources

import com.timplifier.data.base.BasePagingSource
import com.timplifier.data.local.db.daos.CharacterDao
import com.timplifier.data.remote.apiservices.CharacterApiService
import com.timplifier.data.remote.dtos.CharacterDto
import com.timplifier.domain.models.CharacterModel

class CharactersPagingSource(
    private val characterApiService: CharacterApiService,
    private val characterDao: CharacterDao,
    private val name: String?,
    private val status: String?,
    private val species: String?,
    private val gender: String?
) :
    BasePagingSource<CharacterDto, CharacterModel>({
        characterApiService.fetchCharacters(it, name, status, species, gender).also { characters ->
            characterDao.insertCharacters(*characters.results.toTypedArray())
        }
    })