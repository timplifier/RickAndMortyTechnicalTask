package com.timplifier.main.presentation.ui.fragments.main.characters

import android.net.Uri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.timplifier.core.base.BaseFragment
import com.timplifier.core.base.BaseLoadStateAdapter
import com.timplifier.core.extensions.bindViewsToPagingLoadStates
import com.timplifier.core.extensions.directionsSafeNavigation
import com.timplifier.core.extensions.loge
import com.timplifier.main.R
import com.timplifier.main.databinding.FragmentCharactersBinding
import com.timplifier.main.presentation.models.toUI
import com.timplifier.main.presentation.ui.adapters.CharactersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CharactersFragment :
    BaseFragment<FragmentCharactersBinding, CharactersViewModel>(R.layout.fragment_characters) {
    override val binding by viewBinding(FragmentCharactersBinding::bind)
    override val viewModel by viewModels<CharactersViewModel>()
    private val charactersAdapter = CharactersAdapter(this::onItemClick, this::fetchFirstSeenIn)

    override fun initialize() {
        constructRecycler()
    }

    private fun constructRecycler() = with(binding) {
        rvCharacters.layoutManager = LinearLayoutManager(requireContext())
        rvCharacters.adapter = charactersAdapter.withLoadStateFooter(BaseLoadStateAdapter())
        charactersAdapter.bindViewsToPagingLoadStates(rvCharacters, cpiCharacters)
    }

    override fun launchObservers() {
        subscribeToCharacters()
//        subscribeToLocalCharacters()
    }

    private fun subscribeToLocalCharacters() {
        safeFlowGather {
            viewModel.localCharactersState.collectLatest {
                charactersAdapter.submitData(PagingData.from(it))
            }
        }
    }

    private fun subscribeToCharacters() {
        viewModel.fetchCharacters().spectatePaging {
            charactersAdapter.submitData(it)
        }
    }

    private fun onItemClick(id: Int) {
        findNavController().directionsSafeNavigation(
            CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailFragment(
                id
            )
        )
    }

    private fun fetchFirstSeenIn(position: Int, episodeUrl: String) {
        viewModel.fetchSingleEpisode(getIdFromEpisodeUrl(episodeUrl))
            .safeFlowGather(actionIfEitherIsRight = {
                charactersAdapter.renderCharacterFirstSeenIn(position, it.toUI().name)
            }, actionIfEitherIsLeft = {
                loge(msg = it)
            })
    }

    private fun getIdFromEpisodeUrl(episodeUrl: String) =
        Uri.parse(episodeUrl).lastPathSegment.toString().toInt()

//    override fun onNetworkConnectionChanged(isConnected: Boolean) {
//        when (isConnected) {
//            true -> subscribeToCharacters()
//            false -> viewModel.getLocalCharacters()
//        }
//    }
}