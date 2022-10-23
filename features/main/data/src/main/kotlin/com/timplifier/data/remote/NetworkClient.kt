package com.timplifier.data.remote

import com.timplifier.data.remote.apiservices.CharactersApiService
import retrofit2.Retrofit
import javax.inject.Inject

class NetworkClient @Inject constructor() {
    private val retrofit =
        provideRetrofit(
            provideOkHttpClientBuilder().build()
        )

    fun generateCharactersApiService(): CharactersApiService = retrofit.createAnApi()
}

inline fun <reified T : Any> Retrofit.createAnApi(): T = create(T::class.java)