package com.timplifier.main.presentation.models.states.characters

import com.timplifier.main.presentation.models.CharacterFilter

sealed class CharactersSideEffect {

    data class NavigationToFilterDialog(val characterFilter: CharacterFilter? = null) :
        CharactersSideEffect()

    data class NavigationToCharacterDetails(val characterId: Int) : CharactersSideEffect()

    data object DoNotShowAnymoreWhenNoInternetIsClicked : CharactersSideEffect()

    data object ShowLocalDataWhenNoInternetIsClicked : CharactersSideEffect()
}