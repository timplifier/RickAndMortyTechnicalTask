package com.timplifier.main.presentation.models.states.characterDetails

sealed class CharacterDetailsTurn {
    data class FetchSingleCharacter(val id: Int) : CharacterDetailsTurn()
    data class GetSingleCharacter(val id: Int) : CharacterDetailsTurn()
}