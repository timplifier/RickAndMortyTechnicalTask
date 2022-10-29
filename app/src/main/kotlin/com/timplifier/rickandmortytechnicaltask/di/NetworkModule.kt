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