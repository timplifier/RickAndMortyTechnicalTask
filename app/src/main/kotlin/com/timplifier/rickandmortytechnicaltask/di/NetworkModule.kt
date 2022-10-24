package com.timplifier.rickandmortytechnicaltask.di

import com.timplifier.data.remote.NetworkClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun generateCharactersApiService(networkClient: NetworkClient) =
        networkClient.generateCharacterApiService()

    @Singleton
    @Provides
    fun generateEpisodeApiService(networkClient: NetworkClient) =
        networkClient.generateEpisodeApiService()
}