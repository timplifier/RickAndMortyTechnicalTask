package com.timplifier.data.remote.apiservices

import com.timplifier.data.base.BasePagingResponse
import com.timplifier.data.remote.dtos.CharacterDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersApiService {
    @GET("api/character")
    suspend fun fetchCharacters(@Query("page") page: Int): BasePagingResponse<CharacterDto>
}