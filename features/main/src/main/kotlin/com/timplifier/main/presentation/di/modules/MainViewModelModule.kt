package com.timplifier.main.presentation.di.modules

import androidx.lifecycle.ViewModel
import com.timplifier.core.di.annotations.ViewModelKey
import com.timplifier.main.presentation.ui.dialogs.FilterViewModel
import com.timplifier.main.presentation.ui.fragments.main.characters.CharactersViewModel
import com.timplifier.main.presentation.ui.fragments.main.characters.detail.CharacterDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CharactersViewModel::class)
    abstract fun bindCharactersViewModel(charactersViewModel: CharactersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CharacterDetailViewModel::class)
    abstract fun bindCharacterDetailViewModel(characterDetailViewModel: CharacterDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FilterViewModel::class)
    abstract fun bindFilterViewModel(filterViewModel: FilterViewModel): ViewModel
}