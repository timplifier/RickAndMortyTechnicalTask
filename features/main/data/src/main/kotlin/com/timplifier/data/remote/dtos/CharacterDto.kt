package com.timplifier.data.remote.dtos


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.timplifier.data.utils.DataMapper
import com.timplifier.domain.models.CharacterModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class CharacterDto(
    @SerialName("id")
    @PrimaryKey(autoGenerate = false)
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

fun CharacterModel.toData() = CharacterDto(
    id,
    name,
    status,
    species,
    type,
    gender,
    origin.toData(),
    location.toData(),
    image,
    episode,
    url,
    created
)