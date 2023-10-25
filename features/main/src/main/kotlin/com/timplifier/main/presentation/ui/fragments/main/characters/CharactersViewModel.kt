package com.timplifier.main.presentation.ui.fragments.main.characters

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.timplifier.common.either.Either
import com.timplifier.core.base.ViewModel
import com.timplifier.domain.useCases.FetchCharactersUseCase
import com.timplifier.domain.useCases.FetchSingleEpisodeUseCase
import com.timplifier.domain.useCases.GetCharactersUseCase
import com.timplifier.domain.useCases.GetSingleEpisodeUseCase
import com.timplifier.main.presentation.models.states.characters.CharactersSideEffect
import com.timplifier.main.presentation.models.states.characters.CharactersState
import com.timplifier.main.presentation.models.states.characters.CharactersTurn
import com.timplifier.main.presentation.models.toUI
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

class CharactersViewModel @Inject constructor(
    private val fetchCharactersUseCase: FetchCharactersUseCase,
    private val fetchSingleEpisodeUseCase: FetchSingleEpisodeUseCase,
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getSingleEpisodeUseCase: GetSingleEpisodeUseCase
) :
    ViewModel.TurnViewModel<CharactersState, CharactersTurn, CharactersSideEffect>(CharactersState()) {

    override fun processTurn(turn: CharactersTurn) {
        intent {
            when (turn) {
                is CharactersTurn.FetchCharacters -> {
                    fetchCharactersUseCase(
                        state.searchQuery,
                        turn.filter?.status,
                        turn.filter?.species,
                        turn.filter?.gender
                    ).gatherPagingRequest {
                        it.toUI()
                    }.collectLatest {
                        reduce {
                            state.copy(characters = it)
                        }
                    }
                }

                is CharactersTurn.FetchSingleEpisode -> {
                    fetchSingleEpisodeUseCase(turn.id).collectLatest {
                        when (it) {
                            is Either.Left -> {}
                            is Either.Right -> {
                                reduce {
                                    state.copy(
                                        episode = it.value.toUI(),
                                        episodePosition = turn.position
                                    )
                                }
                            }
                        }
                    }
                }

                is CharactersTurn.GetCharacters -> {
                    viewModelScope.launch {
                        getCharactersUseCase(
                            state.searchQuery,
                            turn.filter?.status,
                            turn.filter?.species,
                            turn.filter?.gender
                        ).collectLatest {
                            reduce {
                                state.copy(characters = PagingData.from(it.map { characterModel -> characterModel.toUI() }))
                            }
                        }
                    }
                }

                is CharactersTurn.GetSingleEpisode -> {
                    getSingleEpisodeUseCase(turn.url).collectLatest {
                        reduce {
                            state.copy(
                                episode = it.toUI(),
                                episodePosition = turn.position
                            )
                        }
                    }
                }

                is CharactersTurn.ModifySearchQuery -> {
                    reduce {
                        state.copy(
                            searchQuery = turn.query,
                            characters = PagingData.empty()
                        )
                    }
                }
            }
        }
    }
}