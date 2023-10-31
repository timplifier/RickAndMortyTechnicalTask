package com.timplifier.data.di.modules

import com.timplifier.data.local.db.RealmManager
import com.timplifier.data.local.db.realms.CharacterRealm
import com.timplifier.data.local.db.realms.EpisodeRealm
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object LocalModule {

    @Singleton
    private val realmManager = RealmManager()

    @Singleton
    @Provides
    fun generateCharacterRealm() = CharacterRealm(realmManager.realm)

    @Singleton
    @Provides
    fun generateEpisodeRealm() = EpisodeRealm(realmManager.realm)
}