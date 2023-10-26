package com.timplifier.data.remote.dtos


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.timplifier.data.fragment.CharacterFragment
import com.timplifier.data.utils.DataMapper
import com.timplifier.domain.models.CharacterModel

@Entity
data class CharacterDto(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("species")
    val species: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("origin")
    val origin: OriginDto,
    @SerializedName("location")
    val location: LocationDto,
    @SerializedName("episode")
    val episode: List<EpisodeDto>,
    @SerializedName("image")
    val image: String,
    @SerializedName("created")
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
        episode.map { it.toDomain() },
        image,
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
    episode.map { it.toData() },
    image,
    created
)

fun CharacterFragment.toData() = CharacterDto(
    id,
    name,
    status,
    species,
    type,
    gender,
    origin.toData(),
    location.toData(),
    episode.map { it!!.toData() },
    image, created
)