package com.timplifier.data.remote

import com.timplifier.data.remote.apiservices.CharacterApiService
import com.timplifier.data.remote.apiservices.EpisodeApiService
import retrofit2.Retrofit
import javax.inject.Inject

class NetworkClient @Inject constructor() {
    private val retrofit =
        provideRetrofit(
            provideOkHttpClientBuilder().build()
        )

    fun generateCharacterApiService(): CharacterApiService = retrofit.createAnApi()

    fun generateEpisodeApiService(): EpisodeApiService = retrofit.createAnApi()

    private inline fun <reified T : Any> Retrofit.createAnApi(): T = create(T::class.java)
}