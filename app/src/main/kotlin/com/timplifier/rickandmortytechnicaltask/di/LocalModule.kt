package com.timplifier.rickandmortytechnicaltask.di

import android.content.Context
import com.timplifier.data.local.db.RickAndMortyDatabase
import com.timplifier.data.local.db.RoomManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    private val roomManager = RoomManager()

    @Singleton
    @Provides
    fun generateRoomDatabase(@ApplicationContext context: Context) =
        roomManager.generateRoomDatabase(context)

    @Singleton
    @Provides
    fun generateCharacterDao(rickAndMortyDatabase: RickAndMortyDatabase) =
        roomManager.generateCharacterDao(rickAndMortyDatabase)

    @Singleton
    @Provides
    fun generateEpisodeDao(rickAndMortyDatabase: RickAndMortyDatabase) =
        roomManager.generateEpisodeDao(rickAndMortyDatabase)
}