package com.timplifier.data.local.db.dtos

import com.timplifier.data.utils.DataMapper
import com.timplifier.domain.models.EpisodeModel
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mongodb.kbson.ObjectId

@Serializable
class EpisodeRealmDto(
    @SerialName("id")
    var id: Int,
    @PrimaryKey
    var primaryKey: ObjectId,
    @SerialName("name")
    var name: String,
    @SerialName("air_date")
    var airDate: String,
    @SerialName("episode")
    var episode: String,
    @SerialName("characters")
    var characters: RealmList<String>,
    @SerialName("url")
    var url: String,
    @SerialName("created")
    var created: String
) : DataMapper<EpisodeModel>, RealmObject {

    override fun toDomain() = EpisodeModel(id, name, airDate, episode, characters, url, created)

    constructor() : this(
        id = 1,
        primaryKey = ObjectId.invoke(),
        name = "",
        airDate = "",
        episode = "",
        characters = realmListOf(),
        url = "",
        created = ""
    )
}