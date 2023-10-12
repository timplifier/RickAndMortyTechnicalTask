package com.timplifier.main.presentation.models.states.characterDetails

import com.timplifier.core.ui.state.UIState
import com.timplifier.main.presentation.models.CharacterUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class CharacterDetailsState(
    val character: Flow<UIState<CharacterUI>> = flow {}
)