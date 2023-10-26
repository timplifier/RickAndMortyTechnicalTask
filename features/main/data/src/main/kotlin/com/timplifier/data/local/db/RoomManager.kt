package com.timplifier.data.local.db

import android.content.Context
import androidx.room.Room
import com.timplifier.data.local.db.daos.CharacterDao

class RoomManager {
    fun generateRoomDatabase(context: Context) = Room
        .databaseBuilder(context, RickAndMortyDatabase::class.java, "rickAndMortyDatabase")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    fun generateCharacterDao(roomDatabase: RickAndMortyDatabase): CharacterDao =
        roomDatabase.characterDao()
}