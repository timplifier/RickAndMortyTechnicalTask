package com.timplifier.main.presentation.ui.fragments.main.characters.detail

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.timplifier.core.base.BaseFragment
import com.timplifier.main.R
import com.timplifier.main.databinding.FragmentCharacterDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterDetailFragment :
    BaseFragment<FragmentCharacterDetailBinding, CharacterDetailViewModel>(R.layout.fragment_character_detail) {
    override val binding by viewBinding(FragmentCharacterDetailBinding::bind)
    override val viewModel by viewModels<CharacterDetailViewModel>()
}