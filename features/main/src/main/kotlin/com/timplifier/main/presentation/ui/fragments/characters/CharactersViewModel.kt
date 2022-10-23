package com.timplifier.main.presentation.ui.fragments.characters

import com.timplifier.core.base.BaseViewModel
import com.timplifier.domain.useCases.FetchCharactersUseCase
import com.timplifier.main.presentation.models.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val fetchCharactersUseCase: FetchCharactersUseCase
) :
    BaseViewModel() {
    fun fetchCharacters() = fetchCharactersUseCase().gatherPagingRequest {
        it.toUI()
    }
}