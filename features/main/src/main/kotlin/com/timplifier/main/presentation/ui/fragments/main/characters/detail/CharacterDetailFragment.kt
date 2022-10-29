package com.timplifier.main.presentation.ui.fragments.main.characters.detail

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.timplifier.core.base.BaseFragment
import com.timplifier.core.extensions.loadImageWithGlide
import com.timplifier.core.utils.InternetConnectivityManager
import com.timplifier.main.R
import com.timplifier.main.databinding.FragmentCharacterDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CharacterDetailFragment :
    BaseFragment<FragmentCharacterDetailBinding, CharacterDetailViewModel>(
        R.layout.fragment_character_detail,
    ) {
    override val binding by viewBinding(FragmentCharacterDetailBinding::bind)
    override val viewModel by viewModels<CharacterDetailViewModel>()
    private val args by navArgs<CharacterDetailFragmentArgs>()
    private val isConnectedToTheInternet: InternetConnectivityManager by lazy {
        InternetConnectivityManager(requireActivity())
    }

    override fun launchObservers(): Unit = with(binding) {
        isConnectedToTheInternet.observe(viewLifecycleOwner) {
            viewModel.apply {
                when (it) {
                    true -> {
                        fetchSingleCharacter(args.characterId)
                        characterState.spectateUiState(success = {
                            imCharacter.loadImageWithGlide(it.image)
                            tvCharacterName.text = it.name
                        }, gatherIfSucceed = {
                            it.assembleViewVisibility(gCharacterInfo, cpiCharacterDetail)
                        })
                    }
                    else -> {
                        getSingleCharacter(args.characterId)
                        safeFlowGather {
                            localCharacterState.collectLatest {
                                it?.let { character ->
                                    imCharacter.loadImageWithGlide(character.image)
                                    tvCharacterName.text = character.name
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}