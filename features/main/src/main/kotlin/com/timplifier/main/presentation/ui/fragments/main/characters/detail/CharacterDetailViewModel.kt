package com.timplifier.main.presentation.ui.fragments.main.characters.detail

import androidx.lifecycle.viewModelScope
import com.timplifier.core.base.BaseViewModel
import com.timplifier.domain.useCases.FetchSingleCharacterUseCase
import com.timplifier.domain.useCases.GetSingleCharacterUseCase
import com.timplifier.main.presentation.models.CharacterUI
import com.timplifier.main.presentation.models.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val fetchSingleCharacterUseCase: FetchSingleCharacterUseCase,
    private val getSingleCharacterUseCase: GetSingleCharacterUseCase
) : BaseViewModel() {

    private val _characterState = mutableUiStateFlow<CharacterUI>()
    val characterState = _characterState.asStateFlow()

    private val _localCharacterState = MutableStateFlow<CharacterUI?>(null)
    val localCharacterState = _localCharacterState.asStateFlow()

    fun fetchSingleCharacter(id: Int) =
        fetchSingleCharacterUseCase(id).gatherRequest(_characterState) { it.toUI() }

    fun getSingleCharacter(id: Int) {
        viewModelScope.launch {
            getSingleCharacterUseCase(id).collect {
                _localCharacterState.value = it.toUI()
            }
        }
    }
}