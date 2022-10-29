package com.timplifier.main.presentation.models

import com.timplifier.domain.models.EpisodeModel

data class EpisodeUI(
    val id: Int,
    val name: String,
    val airDate: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String
)

fun EpisodeModel.toUI() = EpisodeUI(id, name, airDate, episode, characters, url, created)