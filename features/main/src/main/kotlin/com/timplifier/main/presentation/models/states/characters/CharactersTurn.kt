package com.timplifier.main.presentation.models.states.characters

import com.timplifier.main.presentation.models.CharacterFilter

sealed class CharactersTurn {
    data class FetchCharacters(val filter: CharacterFilter? = null) : CharactersTurn()
    data class GetCharacters(val filter: CharacterFilter? = null) : CharactersTurn()
    data class ModifySearchQuery(val query: String? = null) : CharactersTurn()
}