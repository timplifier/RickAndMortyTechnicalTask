package com.timplifier.rickandmortytechnicaltask.di

import com.timplifier.data.repositories.CharactersRepositoryImpl
import com.timplifier.data.repositories.EpisodeRepositoryImpl
import com.timplifier.domain.repositories.CharactersRepository
import com.timplifier.domain.repositories.EpisodeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindCharactersRepository(charactersRepositoryImpl: CharactersRepositoryImpl): CharactersRepository

    @Singleton
    @Binds
    abstract fun bindEpisodeRepository(episodeRepositoryImpl: EpisodeRepositoryImpl)  : EpisodeRepository
}