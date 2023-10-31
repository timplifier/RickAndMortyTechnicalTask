package com.timplifier.data.remote.apiservices

import com.timplifier.data.base.BasePagingResponse
import com.timplifier.data.base.get
import com.timplifier.data.extensions.append
import com.timplifier.data.remote.dtos.CharacterDto
import io.ktor.client.HttpClient
import io.ktor.http.path
import io.ktor.util.StringValues

class CharacterApiService(private val httpClient: HttpClient) {
    suspend fun fetchCharacters(
        page: Int,
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ) = get<BasePagingResponse<CharacterDto>>(httpClient) {
        path("api/character/")
        parameters.appendAll(StringValues.build {
            append("page", page)
            append("name", name)
            append("status", status)
            append("species", species)
            append("gender", gender)
        })
    }

    suspend fun fetchSingleCharacter(id: Int) =
        get<CharacterDto>(httpClient) {
            path("api/character/${id}/")
        }.toDomain()
}
