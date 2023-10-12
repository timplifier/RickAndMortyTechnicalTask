package com.timplifier.main.presentation.ui.fragments.main.characters.detail

import com.timplifier.common.either.Either
import com.timplifier.core.base.BaseViewModel
import com.timplifier.core.ui.state.UIState
import com.timplifier.domain.useCases.FetchSingleCharacterUseCase
import com.timplifier.domain.useCases.GetSingleCharacterUseCase
import com.timplifier.main.presentation.models.states.characterDetails.CharacterDetailsState
import com.timplifier.main.presentation.models.toUI
import kotlinx.coroutines.flow.flow
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

class CharacterDetailViewModel @Inject constructor(
    private val fetchSingleCharacterUseCase: FetchSingleCharacterUseCase,
    private val getSingleCharacterUseCase: GetSingleCharacterUseCase
) : BaseViewModel<CharacterDetailsState, Nothing>(CharacterDetailsState()) {

    fun fetchSingleCharacter(id: Int) = intent {
        reduce {
            state.copy(character = flow {
                emit(UIState.Loading())
                fetchSingleCharacterUseCase(id).collect {
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

    fun getSingleCharacter(id: Int) = intent {
        reduce {
            state.copy(character = flow {
                emit(UIState.Loading())
                getSingleCharacterUseCase(id).collect {
                    emit(UIState.Success(it.toUI()))
                }
            })
        }
    }
}