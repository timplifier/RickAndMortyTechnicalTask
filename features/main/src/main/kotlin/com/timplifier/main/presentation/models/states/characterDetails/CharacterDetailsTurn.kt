package com.timplifier.main.presentation.models.states.characterDetails

sealed class CharacterDetailsTurn {
    data class FetchSingleCharacter(val id: String) : CharacterDetailsTurn()
    data class GetSingleCharacter(val id: String) : CharacterDetailsTurn()
}