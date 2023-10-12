package com.timplifier.main.presentation.models.states.filter

import com.timplifier.main.presentation.models.CharacterFilter

sealed class FilterSideEffect {
    data object ResetFilters : FilterSideEffect()

    data class ApplyFilters(val characterFilter: CharacterFilter) : FilterSideEffect()
}