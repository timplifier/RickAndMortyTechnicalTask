package com.timplifier.data.remote.apiservices

import com.timplifier.data.base.get
import com.timplifier.data.remote.dtos.EpisodeDto
import io.ktor.client.HttpClient
import io.ktor.http.path

class EpisodeApiService(private val httpClient: HttpClient) {
    suspend fun fetchSingleEpisode(id: Int) =
        get<EpisodeDto>(httpClient) {
            path("api/episode/${id}")
        }.toDomain()
}