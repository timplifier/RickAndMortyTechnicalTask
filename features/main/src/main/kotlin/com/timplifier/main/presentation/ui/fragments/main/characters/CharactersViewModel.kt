package com.timplifier.main.presentation.ui.fragments.main.characters

import androidx.paging.PagingData
import com.timplifier.core.base.ViewModel
import com.timplifier.domain.useCases.FetchCharactersUseCase
import com.timplifier.domain.useCases.GetCharactersUseCase
import com.timplifier.main.presentation.models.states.characters.CharactersSideEffect
import com.timplifier.main.presentation.models.states.characters.CharactersState
import com.timplifier.main.presentation.models.states.characters.CharactersTurn
import com.timplifier.main.presentation.models.toUI
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

class CharactersViewModel @Inject constructor(
    private val fetchCharactersUseCase: FetchCharactersUseCase,
    private val getCharactersUseCase: GetCharactersUseCase,
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
                    ).gatherPagingRequest({
                        it.toUI()
                    }) {
                        reduce {
                            state.copy(characters = it)
                        }
                    }
                }

                is CharactersTurn.GetCharacters -> {
                    getCharactersUseCase(
                        state.searchQuery,
                        turn.filter?.status,
                        turn.filter?.species,
                        turn.filter?.gender
                    ).gatherLocalRequestNoUIState({
                        it.map { characterModel -> characterModel.toUI() }
                    }) {
                        reduce {
                            state.copy(characters = PagingData.from(it))
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