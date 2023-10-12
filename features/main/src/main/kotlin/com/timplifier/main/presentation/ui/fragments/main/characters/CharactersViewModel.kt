package com.timplifier.main.presentation.ui.fragments.main.characters

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.timplifier.core.base.BaseViewModel
import com.timplifier.domain.useCases.FetchCharactersUseCase
import com.timplifier.domain.useCases.FetchSingleEpisodeUseCase
import com.timplifier.domain.useCases.GetCharactersUseCase
import com.timplifier.domain.useCases.GetSingleEpisodeUseCase
import com.timplifier.main.presentation.models.CharacterFilter
import com.timplifier.main.presentation.models.states.characters.CharactersSideEffect
import com.timplifier.main.presentation.models.states.characters.CharactersState
import com.timplifier.main.presentation.models.toUI
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

class CharactersViewModel @Inject constructor(
    private val fetchCharactersUseCase: FetchCharactersUseCase,
    private val fetchSingleEpisodeUseCase: FetchSingleEpisodeUseCase,
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getSingleEpisodeUseCase: GetSingleEpisodeUseCase
) :
    BaseViewModel<CharactersState, CharactersSideEffect>(CharactersState()) {

    fun navigateToCharactersDetails(characterId: Int) = intent {
        postSideEffect(CharactersSideEffect.NavigationToCharacterDetails(characterId))
    }

    fun navigateToFilterDialog(currentFilter: CharacterFilter?) = intent {
        postSideEffect(CharactersSideEffect.NavigationToFilterDialog(currentFilter))
    }

    fun doNotShowAnymoreInternetConnectionLost() = intent {
        postSideEffect(CharactersSideEffect.DoNotShowAnymoreWhenNoInternetIsClicked)
    }

    fun showLocalDataWhenInternetConnectionLost() = intent {
        postSideEffect(CharactersSideEffect.ShowLocalDataWhenNoInternetIsClicked)
    }

    fun fetchCharacters(
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ) = intent {
        fetchCharactersUseCase(
            state.searchQuery,
            status,
            species,
            gender
        ).gatherPagingRequest {
            it.toUI()
        }.collectLatest {
            reduce {
                state.copy(characters = it)
            }
        }
    }

    fun fetchSingleEpisode(id: Int) =
        fetchSingleEpisodeUseCase(id)

    fun getLocalCharacters(
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ) = intent {
        viewModelScope.launch {
            getCharactersUseCase(state.searchQuery, status, species, gender).collectLatest {
                reduce {
                    state.copy(characters = PagingData.from(it.map { characterModel -> characterModel.toUI() }))
                }
            }
        }
    }

    fun getSingleEpisode(url: String) = getSingleEpisodeUseCase(url)

    fun modifySearchQuery(newQuery: String?) = intent {
        reduce {
            state.copy(
                searchQuery = newQuery,
                characters = PagingData.empty()
            )
        }
    }
}