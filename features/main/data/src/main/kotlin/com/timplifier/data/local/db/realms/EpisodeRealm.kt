package com.timplifier.data.local.db.realms

import com.timplifier.data.extensions.querySingleObject
import com.timplifier.data.extensions.writeSingleObject
import com.timplifier.data.local.db.dtos.EpisodeRealmDto
import com.timplifier.domain.models.EpisodeModel
import io.realm.kotlin.Realm

class EpisodeRealm(private val realm: Realm) {

    suspend fun writeEpisode(episodeRealmDto: EpisodeRealmDto) {
        realm.writeSingleObject(episodeRealmDto)
    }

    fun queryEpisode(url: String) =
        realm.querySingleObject<EpisodeRealmDto, EpisodeModel>(query = "url = $0", url)
}