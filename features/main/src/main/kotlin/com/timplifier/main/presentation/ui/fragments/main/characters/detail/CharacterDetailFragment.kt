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
import com.timplifier.main.presentation.models.CharacterUI
import com.timplifier.main.presentation.models.states.characterDetails.CharacterDetailsState
import com.timplifier.main.presentation.models.states.characterDetails.CharacterDetailsTurn
import org.orbitmvi.orbit.viewmodel.observe
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
        DaggerMainComponent.builder().context(context).build().inject(this)
        super.onAttach(context)
    }

    override fun launchObservers(): Unit = with(binding) {
        viewModel.observe(
            lifecycleOwner = viewLifecycleOwner,
            state = ::render,
        )
        isConnectedToTheInternet.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    viewModel.processTurn(CharacterDetailsTurn.FetchSingleCharacter(args.characterId))
                }

                false -> {
                    viewModel.processTurn(CharacterDetailsTurn.GetSingleCharacter(args.characterId))
                }
            }
        }
    }

    private fun render(characterDetailsState: CharacterDetailsState) {
        safeFlowGather {
            characterDetailsState.character.spectateUiState(success = { character ->
                renderCharacterInfo(character)
            })
        }
    }

    private fun renderCharacterInfo(character: CharacterUI) = with(binding) {
        character.apply {
            imCharacter.loadImageWithGlide(image)
            tvCharacterName.text = name
            cpiCharacterDetail.isVisible =
                image.isEmpty() && name.isEmpty()
        }
    }
}