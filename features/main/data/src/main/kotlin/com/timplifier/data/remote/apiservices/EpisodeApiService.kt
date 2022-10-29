package com.timplifier.data.remote.apiservices

import com.timplifier.data.remote.dtos.EpisodeDto
import retrofit2.http.GET
import retrofit2.http.Path

interface EpisodeApiService {
    @GET("api/episode/{id}")
    suspend fun fetchSingleEpisode(@Path("id") id: Int): EpisodeDto
}