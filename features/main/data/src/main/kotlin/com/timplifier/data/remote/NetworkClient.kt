package com.timplifier.data.remote

import com.timplifier.data.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject

class NetworkClient @Inject constructor() {
    val httpClient = HttpClient(OkHttp) {
        engine {
            preconfigured = provideOkHttpClientBuilder().build()
        }
        defaultRequest {
            url(BuildConfig.BASE_URL)
        }

        install(Resources)
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                prettyPrint = true
            })
        }
    }
}