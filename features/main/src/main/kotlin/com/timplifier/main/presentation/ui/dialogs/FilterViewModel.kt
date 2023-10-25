package com.timplifier.main.presentation.ui.dialogs

import com.timplifier.core.base.ViewModel
import com.timplifier.main.presentation.models.states.filter.FilterSideEffect
import javax.inject.Inject

class FilterViewModel @Inject constructor() :
    ViewModel.NoStateViewModel<FilterSideEffect>()