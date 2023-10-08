package com.timplifier.data.remote.apiservices

import com.timplifier.data.base.BasePagingResponse
import com.timplifier.data.remote.dtos.CharacterDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterApiService {
    @GET("api/character")
    fun fetchCharacters(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("status") status: String?,
        @Query("species") species: String?,
        @Query("gender") gender: String?
    ): Single<BasePagingResponse<CharacterDto>>

    @GET("api/character/{id}")
    fun fetchSingleCharacter(@Path("id") id: Int): Single<CharacterDto>
}