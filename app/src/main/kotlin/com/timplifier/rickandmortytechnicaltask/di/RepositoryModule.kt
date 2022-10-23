package com.timplifier.rickandmortytechnicaltask.di

import com.timplifier.data.repositories.CharactersRepositoryImpl
import com.timplifier.domain.repository.CharactersRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindCharactersRepository(charactersRepositoryImpl: CharactersRepositoryImpl): CharactersRepository
}