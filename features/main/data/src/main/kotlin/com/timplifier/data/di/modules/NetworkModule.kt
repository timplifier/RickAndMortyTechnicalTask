package com.timplifier.data.di.modules

import com.timplifier.data.remote.NetworkClient
import com.timplifier.data.remote.apiservices.CharacterApiService
import com.timplifier.data.remote.apiservices.EpisodeApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object NetworkModule {

    @Singleton
    private val networkClient = NetworkClient()

    @Singleton
    @Provides
    fun generateCharactersApiService() = CharacterApiService(networkClient.httpClient)

    @Singleton
    @Provides
    fun generateEpisodeApiService() = EpisodeApiService(networkClient.httpClient)
}