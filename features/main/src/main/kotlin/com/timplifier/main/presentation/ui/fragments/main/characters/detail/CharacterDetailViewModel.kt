package com.timplifier.main.presentation.ui.fragments.main.characters.detail

import com.timplifier.core.base.ViewModel
import com.timplifier.domain.useCases.FetchSingleCharacterUseCase
import com.timplifier.domain.useCases.GetSingleCharacterUseCase
import com.timplifier.main.presentation.models.states.characterDetails.CharacterDetailsState
import com.timplifier.main.presentation.models.states.characterDetails.CharacterDetailsTurn
import com.timplifier.main.presentation.models.toUI
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

class CharacterDetailViewModel @Inject constructor(
    private val fetchSingleCharacterUseCase: FetchSingleCharacterUseCase,
    private val getSingleCharacterUseCase: GetSingleCharacterUseCase
) : ViewModel.TurnViewModel<CharacterDetailsState, CharacterDetailsTurn, Nothing>(
    CharacterDetailsState()
) {
    override fun processTurn(turn: CharacterDetailsTurn) {
        when (turn) {
            is CharacterDetailsTurn.FetchSingleCharacter -> {
                fetchSingleCharacterUseCase(turn.id).gatherRequest({
                    it.toUI()
                })
                { character ->
                    reduce {
                        state.copy(character = character)
                    }
                }
            }

            is CharacterDetailsTurn.GetSingleCharacter -> {
                getSingleCharacterUseCase(turn.id).gatherLocalRequest({
                    it.toUI()
                }) { character ->
                    reduce {
                        state.copy(character = character)
                    }
                }
            }
        }
    }
}