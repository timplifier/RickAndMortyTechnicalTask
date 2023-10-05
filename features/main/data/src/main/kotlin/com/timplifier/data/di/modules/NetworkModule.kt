package com.timplifier.data.di.modules

import com.timplifier.data.remote.NetworkClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object NetworkModule {

    @Singleton
    private val networkClient = NetworkClient()

    @Singleton
    @Provides
    fun generateCharactersApiService() =
        networkClient.generateCharacterApiService()

    @Singleton
    @Provides
    fun generateEpisodeApiService() =
        networkClient.generateEpisodeApiService()
}