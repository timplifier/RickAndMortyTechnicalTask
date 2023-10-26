package com.timplifier.data.di.modules

import com.timplifier.data.repositories.CharacterRepositoryImpl
import com.timplifier.domain.repositories.CharacterRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindCharactersRepository(charactersRepositoryImpl: CharacterRepositoryImpl): CharacterRepository
}