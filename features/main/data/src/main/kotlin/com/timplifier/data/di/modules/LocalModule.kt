package com.timplifier.data.di.modules

import android.content.Context
import com.timplifier.data.local.db.RickAndMortyDatabase
import com.timplifier.data.local.db.RoomManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object LocalModule {

    @Singleton
    private val roomManager = RoomManager()

    @Singleton
    @Provides
    fun generateRoomDatabase(context: Context) =
        roomManager.generateRoomDatabase(context)

    @Singleton
    @Provides
    fun generateCharacterDao(rickAndMortyDatabase: RickAndMortyDatabase) =
        roomManager.generateCharacterDao(rickAndMortyDatabase)
}