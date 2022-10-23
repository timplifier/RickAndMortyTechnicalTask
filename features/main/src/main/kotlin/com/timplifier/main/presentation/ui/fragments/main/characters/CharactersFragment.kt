package com.timplifier.main.presentation.ui.fragments.main.characters

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.timplifier.core.base.BaseFragment
import com.timplifier.main.R
import com.timplifier.main.databinding.FragmentCharactersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFragment :
    BaseFragment<FragmentCharactersBinding, CharactersViewModel>(R.layout.fragment_characters) {
    override val binding by viewBinding(FragmentCharactersBinding::bind)
    override val viewModel by viewModels<CharactersViewModel>()

    override fun launchObservers() {
        subscribeToCharacters()
    }

    private fun subscribeToCharacters() {
        viewModel.fetchCharacters().spectatePaging {

        }
    }
}