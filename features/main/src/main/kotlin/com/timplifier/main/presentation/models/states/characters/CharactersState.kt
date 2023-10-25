package com.timplifier.main.presentation.models.states.characters

import androidx.paging.PagingData
import com.timplifier.main.presentation.models.CharacterUI
import com.timplifier.main.presentation.models.EpisodeUI

data class CharactersState(
    val characters: PagingData<CharacterUI> = PagingData.empty(),
    val searchQuery: String? = null,
    val episode: EpisodeUI? = null,
    val episodePosition: Int = -1
)