package com.timplifier.main.presentation.ui.fragments.main.characters.detail

import com.timplifier.common.either.Either
import com.timplifier.core.base.ViewModel
import com.timplifier.core.ui.state.UIState
import com.timplifier.domain.useCases.FetchSingleCharacterUseCase
import com.timplifier.domain.useCases.GetSingleCharacterUseCase
import com.timplifier.main.presentation.models.states.characterDetails.CharacterDetailsState
import com.timplifier.main.presentation.models.states.characterDetails.CharacterDetailsTurn
import com.timplifier.main.presentation.models.toUI
import kotlinx.coroutines.flow.flow
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

class CharacterDetailViewModel @Inject constructor(
    private val fetchSingleCharacterUseCase: FetchSingleCharacterUseCase,
    private val getSingleCharacterUseCase: GetSingleCharacterUseCase
) : ViewModel.TurnViewModel<CharacterDetailsState, CharacterDetailsTurn, Nothing>(
    CharacterDetailsState()
) {
    override fun processTurn(turn: CharacterDetailsTurn) {
        intent {
            when (turn) {
                is CharacterDetailsTurn.FetchSingleCharacter -> {
                    reduce {
                        state.copy(character = flow {
                            emit(UIState.Loading())
                            fetchSingleCharacterUseCase(turn.id).collect {
                                when (it) {
                                    is Either.Left -> {
                                        emit(UIState.Error(it.value))
                                    }

                                    is Either.Right -> {
                                        emit(UIState.Success(it.value.toUI()))
                                    }
                                }
                            }
                        })
                    }
                }

                is CharacterDetailsTurn.GetSingleCharacter -> {
                    reduce {
                        state.copy(character = flow {
                            emit(UIState.Loading())
                            getSingleCharacterUseCase(turn.id).collect {
                                emit(UIState.Success(it.toUI()))
                            }
                        })
                    }
                }
            }
        }
    }
}