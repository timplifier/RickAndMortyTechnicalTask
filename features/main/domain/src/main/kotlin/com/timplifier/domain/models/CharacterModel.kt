package com.timplifier.domain.models

data class CharacterModel(
    val id: String,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: OriginModel,
    val location: LocationModel,
    val episode: List<EpisodeModel>,
    val image: String,
    val created: String
)