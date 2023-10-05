package com.timplifier.main.presentation.di.components

import android.content.Context
import com.timplifier.core.di.modules.ViewModelModule
import com.timplifier.main.presentation.di.modules.MainViewModelModule
import com.timplifier.main.presentation.ui.fragments.main.characters.CharactersFragment
import com.timplifier.main.presentation.ui.fragments.main.characters.detail.CharacterDetailFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ViewModelModule::class, MainViewModelModule::class],
)
interface MainComponent {

    fun inject(charactersFragment: CharactersFragment)

    fun inject(characterDetailFragment: CharacterDetailFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): MainComponent
    }
}