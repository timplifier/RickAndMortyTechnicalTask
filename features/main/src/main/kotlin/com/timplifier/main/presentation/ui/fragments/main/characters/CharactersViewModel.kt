package com.timplifier.main.presentation.ui.fragments.main.characters

import androidx.lifecycle.viewModelScope
import com.timplifier.core.base.BaseViewModel
import com.timplifier.domain.useCases.FetchCharactersUseCase
import com.timplifier.domain.useCases.FetchSingleEpisodeUseCase
import com.timplifier.domain.useCases.GetCharactersUseCase
import com.timplifier.main.presentation.models.CharacterUI
import com.timplifier.main.presentation.models.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val fetchCharactersUseCase: FetchCharactersUseCase,
    private val fetchSingleEpisodeUseCase: FetchSingleEpisodeUseCase,
    private val getCharactersUseCase: GetCharactersUseCase
) :
    BaseViewModel() {

    private val _localCharactersState = MutableStateFlow<List<CharacterUI>>(emptyList())
    val localCharactersState = _localCharactersState.asStateFlow()

    fun fetchCharacters() = fetchCharactersUseCase().gatherPagingRequest {
        it.toUI()
    }

    fun fetchSingleEpisode(id: Int) =
        fetchSingleEpisodeUseCase(id)

    fun getLocalCharacters() {
        viewModelScope.launch {
            getCharactersUseCase().collectLatest {
                _localCharactersState.value = it.map { characterModel -> characterModel.toUI() }
            }
        }
    }
}