package com.timplifier.data.remote.dtos


import com.timplifier.data.local.db.dtos.CharacterRealmDto
import com.timplifier.data.utils.DataMapper
import com.timplifier.domain.models.CharacterModel
import io.realm.kotlin.ext.toRealmList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mongodb.kbson.ObjectId

@Serializable
data class CharacterDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("status")
    val status: String,
    @SerialName("species")
    val species: String,
    @SerialName("type")
    val type: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("origin")
    val origin: OriginDto,
    @SerialName("location")
    val location: LocationDto,
    @SerialName("image")
    val image: String,
    @SerialName("episode")
    val episode: List<String>,
    @SerialName("url")
    val url: String,
    @SerialName("created")
    val created: String
) : DataMapper<CharacterModel> {

    override fun toDomain() = CharacterModel(
        id,
        name,
        status,
        species,
        type,
        gender,
        origin.toDomain(),
        location.toDomain(),
        image,
        episode,
        url,
        created
    )
}

fun CharacterDto.toRealm() = CharacterRealmDto(
    ObjectId.invoke(),
    id,
    name,
    status,
    species,
    type,
    gender,
    origin.toRealm(),
    location.toRealm(),
    image,
    episode.toRealmList(),
    url,
    created
)