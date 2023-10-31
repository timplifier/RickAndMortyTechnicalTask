package com.timplifier.data.local.db

import com.timplifier.data.local.db.dtos.CharacterRealmDto
import com.timplifier.data.local.db.dtos.EpisodeRealmDto
import com.timplifier.data.local.db.dtos.LocationRealmDto
import com.timplifier.data.local.db.dtos.OriginRealmDto
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class RealmManager {

    private val config = RealmConfiguration.Builder(
        schema = setOf(
            CharacterRealmDto::class,
            OriginRealmDto::class,
            LocationRealmDto::class,
            EpisodeRealmDto::class
        )
    )
        .schemaVersion(3)
        .build()
    val realm = Realm.open(config)
}