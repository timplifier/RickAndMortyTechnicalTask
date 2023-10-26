package com.timplifier.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.timplifier.data.local.db.converters.Converters
import com.timplifier.data.local.db.daos.CharacterDao
import com.timplifier.data.remote.dtos.CharacterDto
import com.timplifier.data.remote.dtos.EpisodeDto

@Database(entities = [CharacterDto::class, EpisodeDto::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RickAndMortyDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}