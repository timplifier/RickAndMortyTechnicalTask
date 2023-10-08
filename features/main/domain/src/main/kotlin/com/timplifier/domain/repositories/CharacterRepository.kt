package com.timplifier.domain.repositories

import androidx.paging.PagingData
import com.timplifier.common.either.Either
import com.timplifier.domain.models.CharacterModel
import io.reactivex.Flowable
import io.reactivex.Observable

interface CharacterRepository {
    fun fetchCharacters(
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ): Flowable<PagingData<CharacterModel>>

    fun fetchSingleCharacter(id: Int): Observable<Either<String, CharacterModel>>

    fun getLocalCharacters(
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ): Observable<List<CharacterModel>>

    fun getLocalSingleCharacter(id: Int): Observable<CharacterModel>
}