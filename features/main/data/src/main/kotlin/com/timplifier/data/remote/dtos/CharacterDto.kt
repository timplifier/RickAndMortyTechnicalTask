package com.timplifier.data.remote.dtos


import com.google.gson.annotations.SerializedName
import com.timplifier.data.utils.DataMapper
import com.timplifier.domain.models.CharactersModel

data class CharacterDto(
    @SerializedName("id")
    val id: Int,
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
    @SerializedName("image")
    val image: String,
    @SerializedName("episode")
    val episode: List<String>,
    @SerializedName("url")
    val url: String,
    @SerializedName("created")
    val created: String
) : DataMapper<CharactersModel> {

    override fun toDomain() = CharactersModel(
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