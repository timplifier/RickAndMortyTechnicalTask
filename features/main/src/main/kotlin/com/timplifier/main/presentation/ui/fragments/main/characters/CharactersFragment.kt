package com.timplifier.main.presentation.ui.fragments.main.characters

import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.timplifier.core.base.BaseFragment
import com.timplifier.core.base.BaseLoadStateAdapter
import com.timplifier.core.extensions.*
import com.timplifier.core.utils.InternetConnectivityManager
import com.timplifier.data.local.preferences.InternetConnectionPreferencesManager
import com.timplifier.main.R
import com.timplifier.main.databinding.FragmentCharactersBinding
import com.timplifier.main.presentation.models.toUI
import com.timplifier.main.presentation.ui.adapters.CharactersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment :
    BaseFragment<FragmentCharactersBinding, CharactersViewModel>(R.layout.fragment_characters) {
    override val binding by viewBinding(FragmentCharactersBinding::bind)
    override val viewModel by viewModels<CharactersViewModel>()
    private val args by navArgs<CharactersFragmentArgs>()
    private val charactersAdapter = CharactersAdapter(this::onItemClick, this::fetchFirstSeenIn)
    private val isConnectedToInternet: InternetConnectivityManager by lazy {
        InternetConnectivityManager(requireActivity())
    }
    private var isConnected = false

    @Inject
    lateinit var internetConnectionPreferencesManager: InternetConnectionPreferencesManager

    override fun initialize() {
        constructRecycler()
        establishSearch()
    }

    private fun constructRecycler() = with(binding) {
        rvCharacters.layoutManager = LinearLayoutManager(requireContext())
        rvCharacters.adapter = charactersAdapter.withLoadStateFooter(BaseLoadStateAdapter())
        charactersAdapter.bindViewsToPagingLoadStates(
            rvCharacters,
            cpiCharacters,
            tvNoneOfTheCharactersMatchingThisInputWereFound
        )
    }

    private fun establishSearch() {
        binding.svCharacters.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.modifySearchQuery(it)
                    subscribeToCharacters()
                }
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.modifySearchQuery(query?.trim())
                binding.svCharacters.clearFocus()
                subscribeToCharacters()
                return false
            }
        })
    }

    override fun assembleViews() {
        modifySearchView()
        handleFilterVisibility()
    }

    private fun modifySearchView() {
        val searchView: EditText =
            binding.svCharacters.findViewById(androidx.appcompat.R.id.search_src_text)
        searchView.setHintTextColor(Color.WHITE)
        searchView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        val searchClearButton: ImageView =
            binding.svCharacters.findViewById(androidx.appcompat.R.id.search_close_btn)
        searchClearButton.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
    }

    private fun handleFilterVisibility() {
        binding.svCharacters.setOnQueryTextFocusChangeListener { _, hasFocus ->
            binding.imFilter.isVisible = !hasFocus
        }
        binding.svCharacters.setOnCloseListener {
            binding.rvCharacters.scrollToPosition(0)
            args.filter?.status = null
            args.filter?.species = null
            args.filter?.gender = null
            viewModel.modifySearchQuery("")
            when (isConnected) {
                true -> viewModel.fetchCharacters()
                false -> extractDataFromRoom()
            }
            false
        }
    }

    override fun constructListeners() {
        openFilter()
        noInternetConnectionWindowLogic()
    }

    private fun openFilter() {
        binding.imFilter.setOnClickListener {
            findNavController().directionsSafeNavigation(
                CharactersFragmentDirections.actionCharactersFragmentToFilterDialogFragment(
                    args.filter
                )
            )
        }
    }

    private fun noInternetConnectionWindowLogic() {
        doNotShowNoInternetConnectionLayoutAnymore()
        closeNoInternetConnectionLayoutAndLoadSavedData()
    }

    private fun doNotShowNoInternetConnectionLayoutAnymore() = with(binding) {
        iNoInternet.tvDoNotShowAnymore.setOnClickListener {
            internetConnectionPreferencesManager.shouldAwareUserAboutLostInternetConnection = false
            extractDataFromRoom()
        }
    }

    private fun closeNoInternetConnectionLayoutAndLoadSavedData() = with(binding) {
        iNoInternet.tvShowLocalData.setOnClickListener {
            extractDataFromRoom()
        }
    }

    override fun launchObservers() {
        subscribeToInternetConnectionStatus()
        subscribeToFetchedAndLocalCharacters()
        subscribeToSearchQuery()
    }

    private fun subscribeToInternetConnectionStatus() {
        isConnectedToInternet.observe(viewLifecycleOwner) {
            isConnected = it
        }
    }

    private fun subscribeToFetchedAndLocalCharacters() {
        when (isConnected) {
            true -> {
//                viewModel.fetchCharacters()
                subscribeToCharacters()
            }
            false -> {
                viewModel.getLocalCharacters()
                subscribeToLocalCharacters()
            }
        }
//        args.filter?.let {
//            it.apply {
//                when (isConnected) {
//                    true -> {
//                        viewModel.fetchCharacters(status, species, gender)
//                        subscribeToCharacters()
//                    }
//                    false -> {
//                        viewModel.getLocalCharacters(status, species, gender)
//                        subscribeToLocalCharacters()
//                    }
//                }
//            }
//        } ?: when (isConnected) {
//            true -> {
//                viewModel.fetchCharacters()
//                subscribeToCharacters()
//            }
//            false -> {
//                viewModel.getLocalCharacters()
//                subscribeToLocalCharacters()
//            }
//        }
    }

    private fun subscribeToSearchQuery() {
        safeFlowGather {
            viewModel.searchQueryState.collectLatest {
                it?.let {
                    when (isConnected) {
                        true -> viewModel.fetchCharacters()
                        false -> viewModel.getLocalCharacters()
                    }
                }
            }
        }
    }

    private fun subscribeToCharacters() {
        viewModel.fetchCharacters(
            args.filter?.status,
            args.filter?.species,
            args.filter?.gender
        ).spectatePaging {
            charactersAdapter.submitData(it)
        }
    }

    private fun subscribeToLocalCharacters() {
        safeFlowGather {
            viewModel.localCharactersState.collectLatest {
                charactersAdapter.submitData(PagingData.from(it))
            }
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
        when (isConnected) {
            true ->
                try {
                    viewModel.fetchSingleEpisode(getIdFromEpisodeUrl(episodeUrl))
                        .safeFlowGather(actionIfEitherIsRight = {
                            charactersAdapter.renderCharacterFirstSeenIn(
                                position,
                                it.toUI().name
                            )
                        }, actionIfEitherIsLeft = {
                            loge(msg = it)
                        })

                } catch (e: IllegalStateException) {

                } catch (e: IndexOutOfBoundsException) {

                } catch (e: java.lang.NullPointerException) {
                }

            false -> try {
                safeFlowGather {
                    viewModel.getSingleEpisode(episodeUrl).collectLatest {
                        charactersAdapter.renderCharacterFirstSeenIn(position, it.toUI().name)
                    }
                }

            } catch (e: IllegalStateException) {

            } catch (e: IndexOutOfBoundsException) {

            } catch (e: java.lang.NullPointerException) {
            }
        }
    }

    private fun getIdFromEpisodeUrl(episodeUrl: String) =
        Uri.parse(episodeUrl).lastPathSegment.toString().toInt()

    private fun extractDataFromRoom() {
        viewModel.getLocalCharacters(args.filter?.status, args.filter?.species, args.filter?.gender)
        binding.iNoInternet.root.gone()
        binding.appbar.visible()
    }
}