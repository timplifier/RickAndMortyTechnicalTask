package com.timplifier.main.presentation.ui.fragments.main.characters.detail

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.timplifier.core.base.BaseFragment
import com.timplifier.core.extensions.loadImageWithGlide
import com.timplifier.main.R
import com.timplifier.main.databinding.FragmentCharacterDetailBinding
import com.timplifier.main.presentation.receivers.InternetConnectionReceiver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterDetailFragment :
    BaseFragment<FragmentCharacterDetailBinding, CharacterDetailViewModel>(
        R.layout.fragment_character_detail,
    ), InternetConnectionReceiver.InternetConnectionReceiverListener {

    override val binding by viewBinding(FragmentCharacterDetailBinding::bind)
    override val viewModel by viewModels<CharacterDetailViewModel>()
    private val args by navArgs<CharacterDetailFragmentArgs>()

    override fun launchObservers() {
        subscribeToCharacter()
        subscribeToLocalCharacter()
    }

    private fun subscribeToCharacter() = with(binding) {
        viewModel.characterState.spectateUiState(success = {
            imCharacter.loadImageWithGlide(it.image)
            tvCharacterName.text = it.name
        }, gatherIfSucceed = {
            it.assembleViewVisibility(gCharacterInfo, cpiCharacterDetail)
        })
    }

    private fun subscribeToLocalCharacter() = with(binding) {
        safeFlowGather {
            viewModel.localCharacterState.collect {
                imCharacter.loadImageWithGlide(it?.image)
                tvCharacterName.text = it?.name
            }
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        when (isConnected) {
            true -> viewModel.fetchSingleCharacter(args.characterId)
            false -> viewModel.getSingleCharacter(args.characterId)
        }
    }
}