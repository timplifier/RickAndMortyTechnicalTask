package com.timplifier.data.remote.dtos


import com.timplifier.data.local.db.dtos.EpisodeRealmDto
import com.timplifier.data.utils.DataMapper
import com.timplifier.domain.models.EpisodeModel
import io.realm.kotlin.ext.toRealmList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mongodb.kbson.ObjectId

@Serializable
class EpisodeDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("air_date")
    val airDate: String,
    @SerialName("episode")
    val episode: String,
    @SerialName("characters")
    val characters: List<String>,
    @SerialName("url")
    val url: String,
    @SerialName("created")
    val created: String
) : DataMapper<EpisodeModel> {

    override fun toDomain() = EpisodeModel(id, name, airDate, episode, characters, url, created)
}

fun EpisodeDto.toRealm() = EpisodeRealmDto(
    id,
    ObjectId.invoke(),
    name,
    airDate,
    episode,
    characters.toRealmList(),
    url,
    created
)