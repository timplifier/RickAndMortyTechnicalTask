package com.timplifier.main.presentation.models

import com.timplifier.core.base.BaseDiffModel
import com.timplifier.domain.models.CharacterModel

data class CharactersUI(
    override val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: OriginUI,
    val location: LocationUI,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String,
    var firstSeenInEpisode : String = ""
) : BaseDiffModel<Int>

fun CharacterModel.toUI() = CharactersUI(
    id,
    name,
    status,
    species,
    type,
    gender,
    origin.toUI(),
    location.toUI(),
    image,
    episode,
    url,
    created
)