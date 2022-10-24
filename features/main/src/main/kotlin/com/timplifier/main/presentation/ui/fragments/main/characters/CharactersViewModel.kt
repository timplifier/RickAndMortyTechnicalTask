package com.timplifier.main.presentation.ui.fragments.main.characters

import com.timplifier.core.base.BaseViewModel
import com.timplifier.domain.useCases.FetchCharactersUseCase
import com.timplifier.domain.useCases.FetchSingleEpisodeUseCase
import com.timplifier.main.presentation.models.EpisodeUI
import com.timplifier.main.presentation.models.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val fetchCharactersUseCase: FetchCharactersUseCase,
    private val fetchSingleEpisodeUseCase: FetchSingleEpisodeUseCase
) :
    BaseViewModel() {
    private val _firstSeenInEpisodeState = mutableUiStateFlow<EpisodeUI>()
    val firstSeenInEpisodeState = _firstSeenInEpisodeState.asStateFlow()

    fun fetchCharacters() = fetchCharactersUseCase().gatherPagingRequest {
        it.toUI()
    }

    fun fetchSingleEpisode(id: Int) =
        fetchSingleEpisodeUseCase(id)

}