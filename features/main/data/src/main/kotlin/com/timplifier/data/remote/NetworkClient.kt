package com.timplifier.data.remote

import com.timplifier.data.remote.apiservices.CharactersApiService
import retrofit2.Retrofit

class NetworkClient {
    private val retrofit =
        provideRetrofit(
            provideOkHttpClientBuilder().apply {
            }.build()
        )

    fun generateCharactersApiService(): CharactersApiService = retrofit.createAnApi()
}

inline fun <reified T : Any> Retrofit.createAnApi(): T = create(T::class.java)