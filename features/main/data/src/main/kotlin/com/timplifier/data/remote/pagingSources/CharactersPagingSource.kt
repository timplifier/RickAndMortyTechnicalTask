package com.timplifier.data.remote.pagingSources

import com.timplifier.data.CharactersQuery
import com.timplifier.data.base.BasePagingResponse
import com.timplifier.data.base.BasePagingSource
import com.timplifier.data.base.apolloClient
import com.timplifier.data.base.executeQuery
import com.timplifier.data.base.toInfo
import com.timplifier.data.extensions.toOptional
import com.timplifier.data.local.db.daos.CharacterDao
import com.timplifier.data.remote.dtos.CharacterDto
import com.timplifier.data.remote.dtos.toData
import com.timplifier.data.type.FilterCharacter
import com.timplifier.domain.models.CharacterModel

class CharactersPagingSource(
    private val characterDao: CharacterDao,
    private val name: String?,
    private val status: String?,
    private val species: String?,
    private val gender: String?
) :
    BasePagingSource<CharacterDto, CharacterModel>({ page ->
        val charactersQuery = apolloClient.executeQuery(
            CharactersQuery(
                page.toOptional(),
                FilterCharacter(
                    name = name.toOptional(),
                    status = status.toOptional(),
                    species = species.toOptional(),
                    gender = gender.toOptional()
                ).toOptional()
            )
        )
        BasePagingResponse(
            charactersQuery.characters.info.toInfo(),
            charactersQuery.characters.results.map { it!!.characterFragment.toData() }
                .also { characters ->
                    characterDao.insertCharacters(*characters.toTypedArray())
                }
        )
    })