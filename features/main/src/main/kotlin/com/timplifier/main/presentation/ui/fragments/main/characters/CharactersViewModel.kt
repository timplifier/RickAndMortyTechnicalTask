package com.timplifier.main.presentation.ui.fragments.main.characters

import androidx.lifecycle.viewModelScope
import com.timplifier.core.base.BaseViewModel
import com.timplifier.domain.useCases.FetchCharactersUseCase
import com.timplifier.domain.useCases.FetchSingleEpisodeUseCase
import com.timplifier.domain.useCases.GetCharactersUseCase
import com.timplifier.domain.useCases.GetSingleEpisodeUseCase
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
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getSingleEpisodeUseCase: GetSingleEpisodeUseCase
) :
    BaseViewModel() {

    private val _localCharactersState = MutableStateFlow<List<CharacterUI>>(emptyList())
    val localCharactersState = _localCharactersState.asStateFlow()

    private val _searchQueryState = MutableStateFlow<String?>(null)
    val searchQueryState = _searchQueryState.asStateFlow()

    fun fetchCharacters(
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ) =
        fetchCharactersUseCase(
            _searchQueryState.value,
            status,
            species,
            gender
        ).gatherPagingRequest {
            it.toUI()
        }

    fun fetchSingleEpisode(id: Int) =
        fetchSingleEpisodeUseCase(id)

    fun getLocalCharacters(
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ) {
        viewModelScope.launch {
            getCharactersUseCase(_searchQueryState.value, status, species, gender).collectLatest {
                _localCharactersState.value = it.map { characterModel -> characterModel.toUI() }
            }
        }
    }

    fun getSingleEpisode(url: String) = getSingleEpisodeUseCase(url)

    fun modifySearchQuery(newQuery: String?) {
        _searchQueryState.value = newQuery
    }
}