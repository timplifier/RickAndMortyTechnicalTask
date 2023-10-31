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
import com.timplifier.core.utils.DebounceHandler
import com.timplifier.core.utils.InternetConnectivityManager
import com.timplifier.core.utils.ViewModelFactory
import com.timplifier.data.local.preferences.InternetConnectionPreferencesManager
import com.timplifier.main.R
import com.timplifier.main.databinding.FragmentCharactersBinding
import com.timplifier.main.presentation.di.components.DaggerMainComponent
import com.timplifier.main.presentation.models.states.characters.CharactersSideEffect
import com.timplifier.main.presentation.models.states.characters.CharactersState
import com.timplifier.main.presentation.models.states.characters.CharactersTurn
import com.timplifier.main.presentation.ui.adapters.CharactersAdapter
import org.orbitmvi.orbit.viewmodel.observe
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
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    DebounceHandler.postDelayed(500) {
                        viewModel.processTurn(CharactersTurn.ModifySearchQuery(it))
                        subscribeToFetchedOrLocalCharacters()
                        binding.tvNoneOfTheCharactersMatchingThisInputWereFound.isVisible =
                            charactersAdapter.itemCount == 0
                    }
                }
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                DebounceHandler.postDelayed(500) {
                    viewModel.processTurn(CharactersTurn.ModifySearchQuery(query))
                    subscribeToFetchedOrLocalCharacters()
                    binding.tvNoneOfTheCharactersMatchingThisInputWereFound.isVisible =
                        charactersAdapter.itemCount == 0
                }
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
            viewModel.processTurn(CharactersTurn.ModifySearchQuery(""))
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
            viewModel.postSideEffect(CharactersSideEffect.NavigationToFilterDialog(args.filter))
        }
    }

    private fun noInternetConnectionWindowLogic() {
        doNotShowNoInternetConnectionLayoutAnymore()
        closeNoInternetConnectionLayoutAndLoadSavedData()
    }

    private fun doNotShowNoInternetConnectionLayoutAnymore() = with(binding) {
        iNoInternet.tvDoNotShowAnymore.setOnClickListener {
            viewModel.postSideEffect(CharactersSideEffect.DoNotShowAnymoreWhenNoInternetIsClicked)
        }
    }

    private fun closeNoInternetConnectionLayoutAndLoadSavedData() = with(binding) {
        iNoInternet.tvShowLocalData.setOnClickListener {
            viewModel.postSideEffect(CharactersSideEffect.ShowLocalDataWhenNoInternetIsClicked)
        }
    }

    override fun launchObservers() {
        viewModel.observe(viewLifecycleOwner, state = ::render, sideEffect = ::handleSideEffect)
        subscribeToInternetConnectionStatus()
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
                    getLocalCharacters()
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
                fetchCharacters()
            })
    }

    private fun subscribeToFetchedOrLocalCharacters() {
        observeInternetConnectivityStatusAndDoSomethingWhenConnectedAndDisconnected(
            actionWhenConnected = {
                fetchCharacters()
            },
            actionWhenDisconnected = {
                getLocalCharacters()
            })
    }

    private fun onItemClick(id: Int) {
        viewModel.postSideEffect(CharactersSideEffect.NavigationToCharacterDetails(id))
    }

    private fun fetchFirstSeenIn(position: Int, episodeUrl: String) {
        observeInternetConnectivityStatusAndDoSomethingWhenConnectedAndDisconnected(
            actionWhenConnected = {
                tryToDoSomethingAndCatchNullPointerException {
                    viewModel.processTurn(
                        CharactersTurn.FetchSingleEpisode(
                            getIdFromEpisodeUrl(
                                episodeUrl
                            ), position
                        )
                    )
                }
            },
            actionWhenDisconnected = {
                safeFlowGather {
                    tryToDoSomethingAndCatchNullPointerException {
                        viewModel.processTurn(CharactersTurn.GetSingleEpisode(episodeUrl, position))
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
            getLocalCharacters()
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

    private fun render(charactersState: CharactersState) {
        safeFlowGather {
            charactersAdapter.submitData(charactersState.characters)
        }
        charactersAdapter.renderCharacterFirstSeenIn(
            charactersState.episodePosition,
            charactersState.episode?.name.toString()
        )
    }

    private fun handleSideEffect(charactersSideEffect: CharactersSideEffect) {
        when (charactersSideEffect) {
            is CharactersSideEffect.NavigationToCharacterDetails ->
                findNavController().directionsSafeNavigation(
                    CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailFragment(
                        charactersSideEffect.characterId
                    )
                )

            is CharactersSideEffect.NavigationToFilterDialog ->
                findNavController().directionsSafeNavigation(
                    CharactersFragmentDirections.actionCharactersFragmentToFilterDialogFragment(
                        charactersSideEffect.characterFilter
                    )
                )

            is CharactersSideEffect.DoNotShowAnymoreWhenNoInternetIsClicked -> {
                internetConnectionPreferencesManager.shouldAwareUserAboutLostInternetConnection =
                    false
                extractDataFromRoom()
            }

            is CharactersSideEffect.ShowLocalDataWhenNoInternetIsClicked ->
                extractDataFromRoom()
        }
    }

    private fun fetchCharacters() {
        viewModel.processTurn(CharactersTurn.FetchCharacters(args.filter))
    }

    private fun getLocalCharacters() {
        viewModel.processTurn(CharactersTurn.GetCharacters(args.filter))
    }
}