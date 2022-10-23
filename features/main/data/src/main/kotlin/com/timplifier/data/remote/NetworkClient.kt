package com.timplifier.data.remote

import retrofit2.Retrofit

class NetworkClient {
    private val retrofit =
        provideRetrofit(
            provideOkHttpClientBuilder().apply {
            }.build()
        )
}

inline fun <reified T : Any> Retrofit.createAnApi(): T = create(T::class.java)