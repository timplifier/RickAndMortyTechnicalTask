package com.timplifier.main.presentation.ui.fragments.main.characters

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.timplifier.core.base.BaseFragment
import com.timplifier.core.base.BaseLoadStateAdapter
import com.timplifier.core.extensions.bindViewsToPagingLoadStates
import com.timplifier.core.extensions.directionsSafeNavigation
import com.timplifier.core.extensions.gone
import com.timplifier.core.extensions.invisible
import com.timplifier.core.extensions.loge
import com.timplifier.core.extensions.visible
import com.timplifier.core.utils.InternetConnectivityManager
import com.timplifier.core.utils.ViewModelFactory
import com.timplifier.data.local.preferences.InternetConnectionPreferencesManager
import com.timplifier.main.R
import com.timplifier.main.databinding.FragmentCharactersBinding
import com.timplifier.main.presentation.di.components.DaggerMainComponent
import com.timplifier.main.presentation.models.toUI
import com.timplifier.main.presentation.ui.adapters.CharactersAdapter
import javax.inject.Inject

class CharactersFragment :
    BaseFragment<FragmentCharactersBinding, CharactersViewModel>(R.layout.fragment_characters) {
    override val binding by viewBinding(FragmentCharactersBinding::bind)
    override val viewModel by viewModels<CharactersViewModel> {
        ViewModelProvider(this, viewModelFactory)[CharactersViewModel::class.java]
        viewModelFactory
    }
    private val args by navArgs<CharactersFragmentArgs>()
    private val charactersAdapter = CharactersAdapter(this::onItemClick, this::fetchFirstSeenIn)

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory

    private val isConnectedToInternet: InternetConnectivityManager by lazy {
        InternetConnectivityManager(requireActivity())
    }

    @Inject
    lateinit var internetConnectionPreferencesManager: InternetConnectionPreferencesManager

    override fun onAttach(context: Context) {
        DaggerMainComponent.builder().context(context).build().inject(this)
        super.onAttach(context)
    }

    override fun initialize() {
        constructRecycler()
        establishSearch()
    }

    private fun constructRecycler() = with(binding) {
        rvCharacters.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = charactersAdapter.withLoadStateFooter(BaseLoadStateAdapter())
            charactersAdapter.bindViewsToPagingLoadStates(
                this,
                cpiCharacters,
                tvNoneOfTheCharactersMatchingThisInputWereFound
            )
        }
    }

    private fun establishSearch() {
        binding.svCharacters.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.modifySearchQuery(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.modifySearchQuery(query.trim())
                binding.svCharacters.clearFocus()
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

    private fun handleFilterVisibility() = with(binding) {
        svCharacters.setOnQueryTextFocusChangeListener { _, hasFocus ->
            imFilter.isVisible = !hasFocus
        }
        svCharacters.setOnCloseListener {
            args.filter?.status = null
            args.filter?.species = null
            args.filter?.gender = null
            viewModel.modifySearchQuery("")
            rvCharacters.scrollToPosition(0)
            svCharacters.setQuery("", true)
            svCharacters.clearFocus()
            svCharacters.onActionViewCollapsed()
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
            internetConnectionPreferencesManager.shouldAwareUserAboutLostInternetConnection =
                false
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
        subscribeToSearchQuery()
    }

    private fun subscribeToInternetConnectionStatus() = with(binding) {
        observeInternetConnectivityStatusAndDoSomethingWhenConnectedAndDisconnected(
            actionWhenDisconnected = {
                iNoInternet.root.isVisible =
                    internetConnectionPreferencesManager.shouldAwareUserAboutLostInternetConnection
                if (svCharacters.hasFocus())
                    svCharacters.clearFocus()
                svCharacters.setQuery("", true)
                args.filter?.let {
                    iNoInternet.root.invisible()
                    viewModel.getLocalCharacters(
                        args.filter?.status,
                        args.filter?.species,
                        args.filter?.gender
                    )
                    subscribeToLocalCharacters()
                }
                if (iNoInternet.root.isVisible) {
                    appbar.isGone = true
                    rvCharacters.isGone = true
                }
                if (!internetConnectionPreferencesManager.shouldAwareUserAboutLostInternetConnection) {
                    extractDataFromRoom()
                }
            },
            actionWhenConnected = {
                if (iNoInternet.root.isVisible) {
                    iNoInternet.root.gone()
                    appbar.visible()
                    rvCharacters.visible()
                }
                subscribeToCharacters()
            })
    }

    private fun subscribeToFetchedOrLocalCharacters() {
        observeInternetConnectivityStatusAndDoSomethingWhenConnectedAndDisconnected(
            actionWhenConnected = {
                subscribeToCharacters()
            },
            actionWhenDisconnected = {
                viewModel.getLocalCharacters(
                    args.filter?.status,
                    args.filter?.species,
                    args.filter?.gender
                )
                subscribeToLocalCharacters()
            })
    }

    private fun subscribeToSearchQuery() {
        viewModel.searchQueryState.safeObservableGather {
            this?.let {
                subscribeToFetchedOrLocalCharacters()
            } ?: subscribeToFetchedOrLocalCharacters()

            binding.tvNoneOfTheCharactersMatchingThisInputWereFound.isVisible =
                charactersAdapter.itemCount == 0
        }
    }

    private fun subscribeToCharacters() {
        viewModel.fetchCharacters(
            args.filter?.status, args.filter?.species, args.filter?.gender
        ).spectatePaging { pagingData ->
            charactersAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }
    }

    private fun subscribeToLocalCharacters() {
        viewModel.localCharactersState.safeObservableGather {
            charactersAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.from(this))
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
        observeInternetConnectivityStatusAndDoSomethingWhenConnectedAndDisconnected(
            actionWhenConnected = {
                tryToDoSomethingAndCatchNullPointerException {
                    viewModel.fetchSingleEpisode(getIdFromEpisodeUrl(episodeUrl))
                        .safeObservableGather(actionIfEitherIsLeft = {
                            loge(msg = this)
                        }, actionIfEitherIsRight = {
                            charactersAdapter.renderCharacterFirstSeenIn(
                                position,
                                toUI().name
                            )
                        })
                }
            },
            actionWhenDisconnected = {
                safeFlowGather {
                    tryToDoSomethingAndCatchNullPointerException {
                        viewModel.getSingleEpisode(episodeUrl).safeObservableGather {
                            toUI().name.let { name ->
                                charactersAdapter.renderCharacterFirstSeenIn(position, name)
                            }
                        }
                    }
                }
            })
    }

    private fun tryToDoSomethingAndCatchNullPointerException(action: suspend () -> Unit) {
        safeFlowGather {
            try {
                action()
            } catch (nullPointer: NullPointerException) {
                loge(msg = nullPointer.message.toString())
            } catch (indexOutOfBounds: IndexOutOfBoundsException) {
                loge(msg = indexOutOfBounds.message.toString())
            } catch (illegalState: IllegalStateException) {
                loge(msg = illegalState.message.toString())
            }
        }
    }

    private fun getIdFromEpisodeUrl(episodeUrl: String) =
        Uri.parse(episodeUrl).lastPathSegment.toString().toInt()

    private fun extractDataFromRoom() = with(binding) {
        if (iNoInternet.root.isVisible) {
            viewModel.getLocalCharacters(
                args.filter?.status,
                args.filter?.species,
                args.filter?.gender
            )
            subscribeToLocalCharacters()
            iNoInternet.root.gone()
            appbar.visible()
            rvCharacters.visible()
        }
    }

    private fun observeInternetConnectivityStatusAndDoSomethingWhenConnectedAndDisconnected(
        actionWhenConnected: () -> Unit,
        actionWhenDisconnected: () -> Unit
    ) {
        isConnectedToInternet.observe(viewLifecycleOwner) {
            when (it) {
                true -> actionWhenConnected()
                false -> actionWhenDisconnected()
            }
        }
    }
}