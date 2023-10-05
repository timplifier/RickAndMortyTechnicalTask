package com.timplifier.main.presentation.ui.fragments.main.characters.detail

import android.content.Context
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.timplifier.core.base.BaseFragment
import com.timplifier.core.extensions.loadImageWithGlide
import com.timplifier.core.utils.InternetConnectivityManager
import com.timplifier.core.utils.ViewModelFactory
import com.timplifier.main.R
import com.timplifier.main.databinding.FragmentCharacterDetailBinding
import com.timplifier.main.presentation.di.components.DaggerMainComponent
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class CharacterDetailFragment :
    BaseFragment<FragmentCharacterDetailBinding, CharacterDetailViewModel>(
        R.layout.fragment_character_detail,
    ) {
    override val binding by viewBinding(FragmentCharacterDetailBinding::bind)
    override val viewModel by viewModels<CharacterDetailViewModel> {
        ViewModelProvider(this, viewModelFactory)[CharacterDetailViewModel::class.java]
        viewModelFactory
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory
    private val args by navArgs<CharacterDetailFragmentArgs>()
    private val isConnectedToTheInternet: InternetConnectivityManager by lazy {
        InternetConnectivityManager(requireActivity())
    }

    override fun onAttach(context: Context) {
        DaggerMainComponent.builder().context(context).build()
        super.onAttach(context)
    }

    override fun launchObservers(): Unit = with(binding) {
        isConnectedToTheInternet.observe(viewLifecycleOwner) {
            viewModel.apply {
                when (it) {
                    true -> {
                        fetchSingleCharacter(args.characterId)
                        characterState.spectateUiState(success = { character ->
                            character.apply {
                                imCharacter.loadImageWithGlide(image)
                                tvCharacterName.text = name
                                cpiCharacterDetail.isVisible =
                                    image.isEmpty() && name.isEmpty()
                            }
                        })
                    }

                    else -> {
                        getSingleCharacter(args.characterId)
                        safeFlowGather {
                            localCharacterState.collectLatest {
                                it?.let { character ->
                                    character.apply {
                                        imCharacter.loadImageWithGlide(image)
                                        tvCharacterName.text = name
                                        cpiCharacterDetail.isVisible =
                                            image.isEmpty() && name.isEmpty()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}