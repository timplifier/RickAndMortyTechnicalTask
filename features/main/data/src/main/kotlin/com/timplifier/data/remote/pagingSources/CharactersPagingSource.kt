package com.timplifier.data.remote.pagingSources

import com.timplifier.data.base.BaseRxPagingSource
import com.timplifier.data.local.db.daos.CharacterDao
import com.timplifier.data.remote.apiservices.CharacterApiService
import com.timplifier.data.remote.dtos.CharacterDto
import com.timplifier.domain.models.CharacterModel
import io.reactivex.schedulers.Schedulers

class CharactersPagingSource(
    private val characterApiService: CharacterApiService,
    private val characterDao: CharacterDao,
    private val name: String?,
    private val status: String?,
    private val species: String?,
    private val gender: String?
) :
    BaseRxPagingSource<CharacterDto, CharacterModel>({
        characterApiService.fetchCharacters(it, name, status, species, gender).also { characters ->
            characters.subscribeOn(Schedulers.io())
                .map { characterDao.insertCharacters(*it.results.toTypedArray()) }
        }
    })