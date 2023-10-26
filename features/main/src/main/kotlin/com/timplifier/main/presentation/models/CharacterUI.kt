package com.timplifier.main.presentation.models

import com.timplifier.core.base.BaseDiffModel
import com.timplifier.domain.models.CharacterModel

data class CharacterUI(
    override val id: String,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: OriginUI,
    val location: LocationUI,
    val episode: List<EpisodeUI>,
    val image: String,
    val created: String,
    var firstSeenInEpisode: String = ""
) : BaseDiffModel<String>

fun CharacterModel.toUI() = CharacterUI(
    id,
    name,
    status,
    species,
    type,
    gender,
    origin.toUI(),
    location.toUI(),
    episode.map { it.toUI() },
    image,
    created
)