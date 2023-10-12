package com.timplifier.main.presentation.ui.dialogs

import com.timplifier.core.base.BaseViewModel
import com.timplifier.main.presentation.models.CharacterFilter
import com.timplifier.main.presentation.models.states.filter.FilterSideEffect
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import javax.inject.Inject

class FilterViewModel @Inject constructor() : BaseViewModel<Unit, FilterSideEffect>(Unit) {

    fun resetFilters() = intent {
        postSideEffect(FilterSideEffect.ResetFilters)
    }

    fun applyFilters(characterFilter: CharacterFilter) = intent {
        postSideEffect(FilterSideEffect.ApplyFilters(characterFilter))
    }
}