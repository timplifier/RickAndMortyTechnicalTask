package com.timplifier.main.presentation.ui.fragments.main.characters

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.util.Log
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
import com.timplifier.data.base.BasePagingResponse
import com.timplifier.data.local.preferences.InternetConnectionPreferencesManager
import com.timplifier.data.remote.dtos.CharacterDto
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

    private inline fun <reified T> typeToken() = object : TypeToken<T>() {}.type
    private inline fun <reified T> fromJson(value: String?): T =
        Gson().fromJson(value, typeToken<T>())
    private fun constructRecycler() = with(binding) {

        Log.e("gaypop",  fromJson<BasePagingResponse<CharacterDto>>(
            "{\"info\":{\"count\":826,\"pages\":42,\"next\":\"https://rickandmortyapi.com/api/character?page=2\",\"prev\":null},\"results\":[{\"id\":1,\"name\":\"Rick Sanchez\",\"status\":\"Alive\",\"species\":\"Human\",\"type\":\"\",\"gender\":\"Male\",\"origin\":{\"name\":\"Earth (C-137)\",\"url\":\"https://rickandmortyapi.com/api/location/1\"},\"location\":{\"name\":\"Citadel of Ricks\",\"url\":\"https://rickandmortyapi.com/api/location/3\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/1.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/1\",\"https://rickandmortyapi.com/api/episode/2\",\"https://rickandmortyapi.com/api/episode/3\",\"https://rickandmortyapi.com/api/episode/4\",\"https://rickandmortyapi.com/api/episode/5\",\"https://rickandmortyapi.com/api/episode/6\",\"https://rickandmortyapi.com/api/episode/7\",\"https://rickandmortyapi.com/api/episode/8\",\"https://rickandmortyapi.com/api/episode/9\",\"https://rickandmortyapi.com/api/episode/10\",\"https://rickandmortyapi.com/api/episode/11\",\"https://rickandmortyapi.com/api/episode/12\",\"https://rickandmortyapi.com/api/episode/13\",\"https://rickandmortyapi.com/api/episode/14\",\"https://rickandmortyapi.com/api/episode/15\",\"https://rickandmortyapi.com/api/episode/16\",\"https://rickandmortyapi.com/api/episode/17\",\"https://rickandmortyapi.com/api/episode/18\",\"https://rickandmortyapi.com/api/episode/19\",\"https://rickandmortyapi.com/api/episode/20\",\"https://rickandmortyapi.com/api/episode/21\",\"https://rickandmortyapi.com/api/episode/22\",\"https://rickandmortyapi.com/api/episode/23\",\"https://rickandmortyapi.com/api/episode/24\",\"https://rickandmortyapi.com/api/episode/25\",\"https://rickandmortyapi.com/api/episode/26\",\"https://rickandmortyapi.com/api/episode/27\",\"https://rickandmortyapi.com/api/episode/28\",\"https://rickandmortyapi.com/api/episode/29\",\"https://rickandmortyapi.com/api/episode/30\",\"https://rickandmortyapi.com/api/episode/31\",\"https://rickandmortyapi.com/api/episode/32\",\"https://rickandmortyapi.com/api/episode/33\",\"https://rickandmortyapi.com/api/episode/34\",\"https://rickandmortyapi.com/api/episode/35\",\"https://rickandmortyapi.com/api/episode/36\",\"https://rickandmortyapi.com/api/episode/37\",\"https://rickandmortyapi.com/api/episode/38\",\"https://rickandmortyapi.com/api/episode/39\",\"https://rickandmortyapi.com/api/episode/40\",\"https://rickandmortyapi.com/api/episode/41\",\"https://rickandmortyapi.com/api/episode/42\",\"https://rickandmortyapi.com/api/episode/43\",\"https://rickandmortyapi.com/api/episode/44\",\"https://rickandmortyapi.com/api/episode/45\",\"https://rickandmortyapi.com/api/episode/46\",\"https://rickandmortyapi.com/api/episode/47\",\"https://rickandmortyapi.com/api/episode/48\",\"https://rickandmortyapi.com/api/episode/49\",\"https://rickandmortyapi.com/api/episode/50\",\"https://rickandmortyapi.com/api/episode/51\"],\"url\":\"https://rickandmortyapi.com/api/character/1\",\"created\":\"2017-11-04T18:48:46.250Z\"},{\"id\":2,\"name\":\"Morty Smith\",\"status\":\"Alive\",\"species\":\"Human\",\"type\":\"\",\"gender\":\"Male\",\"origin\":{\"name\":\"unknown\",\"url\":\"\"},\"location\":{\"name\":\"Citadel of Ricks\",\"url\":\"https://rickandmortyapi.com/api/location/3\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/2.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/1\",\"https://rickandmortyapi.com/api/episode/2\",\"https://rickandmortyapi.com/api/episode/3\",\"https://rickandmortyapi.com/api/episode/4\",\"https://rickandmortyapi.com/api/episode/5\",\"https://rickandmortyapi.com/api/episode/6\",\"https://rickandmortyapi.com/api/episode/7\",\"https://rickandmortyapi.com/api/episode/8\",\"https://rickandmortyapi.com/api/episode/9\",\"https://rickandmortyapi.com/api/episode/10\",\"https://rickandmortyapi.com/api/episode/11\",\"https://rickandmortyapi.com/api/episode/12\",\"https://rickandmortyapi.com/api/episode/13\",\"https://rickandmortyapi.com/api/episode/14\",\"https://rickandmortyapi.com/api/episode/15\",\"https://rickandmortyapi.com/api/episode/16\",\"https://rickandmortyapi.com/api/episode/17\",\"https://rickandmortyapi.com/api/episode/18\",\"https://rickandmortyapi.com/api/episode/19\",\"https://rickandmortyapi.com/api/episode/20\",\"https://rickandmortyapi.com/api/episode/21\",\"https://rickandmortyapi.com/api/episode/22\",\"https://rickandmortyapi.com/api/episode/23\",\"https://rickandmortyapi.com/api/episode/24\",\"https://rickandmortyapi.com/api/episode/25\",\"https://rickandmortyapi.com/api/episode/26\",\"https://rickandmortyapi.com/api/episode/27\",\"https://rickandmortyapi.com/api/episode/28\",\"https://rickandmortyapi.com/api/episode/29\",\"https://rickandmortyapi.com/api/episode/30\",\"https://rickandmortyapi.com/api/episode/31\",\"https://rickandmortyapi.com/api/episode/32\",\"https://rickandmortyapi.com/api/episode/33\",\"https://rickandmortyapi.com/api/episode/34\",\"https://rickandmortyapi.com/api/episode/35\",\"https://rickandmortyapi.com/api/episode/36\",\"https://rickandmortyapi.com/api/episode/37\",\"https://rickandmortyapi.com/api/episode/38\",\"https://rickandmortyapi.com/api/episode/39\",\"https://rickandmortyapi.com/api/episode/40\",\"https://rickandmortyapi.com/api/episode/41\",\"https://rickandmortyapi.com/api/episode/42\",\"https://rickandmortyapi.com/api/episode/43\",\"https://rickandmortyapi.com/api/episode/44\",\"https://rickandmortyapi.com/api/episode/45\",\"https://rickandmortyapi.com/api/episode/46\",\"https://rickandmortyapi.com/api/episode/47\",\"https://rickandmortyapi.com/api/episode/48\",\"https://rickandmortyapi.com/api/episode/49\",\"https://rickandmortyapi.com/api/episode/50\",\"https://rickandmortyapi.com/api/episode/51\"],\"url\":\"https://rickandmortyapi.com/api/character/2\",\"created\":\"2017-11-04T18:50:21.651Z\"},{\"id\":3,\"name\":\"Summer Smith\",\"status\":\"Alive\",\"species\":\"Human\",\"type\":\"\",\"gender\":\"Female\",\"origin\":{\"name\":\"Earth (Replacement Dimension)\",\"url\":\"https://rickandmortyapi.com/api/location/20\"},\"location\":{\"name\":\"Earth (Replacement Dimension)\",\"url\":\"https://rickandmortyapi.com/api/location/20\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/3.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/6\",\"https://rickandmortyapi.com/api/episode/7\",\"https://rickandmortyapi.com/api/episode/8\",\"https://rickandmortyapi.com/api/episode/9\",\"https://rickandmortyapi.com/api/episode/10\",\"https://rickandmortyapi.com/api/episode/11\",\"https://rickandmortyapi.com/api/episode/12\",\"https://rickandmortyapi.com/api/episode/14\",\"https://rickandmortyapi.com/api/episode/15\",\"https://rickandmortyapi.com/api/episode/16\",\"https://rickandmortyapi.com/api/episode/17\",\"https://rickandmortyapi.com/api/episode/18\",\"https://rickandmortyapi.com/api/episode/19\",\"https://rickandmortyapi.com/api/episode/20\",\"https://rickandmortyapi.com/api/episode/21\",\"https://rickandmortyapi.com/api/episode/22\",\"https://rickandmortyapi.com/api/episode/23\",\"https://rickandmortyapi.com/api/episode/24\",\"https://rickandmortyapi.com/api/episode/25\",\"https://rickandmortyapi.com/api/episode/26\",\"https://rickandmortyapi.com/api/episode/27\",\"https://rickandmortyapi.com/api/episode/29\",\"https://rickandmortyapi.com/api/episode/30\",\"https://rickandmortyapi.com/api/episode/31\",\"https://rickandmortyapi.com/api/episode/32\",\"https://rickandmortyapi.com/api/episode/33\",\"https://rickandmortyapi.com/api/episode/34\",\"https://rickandmortyapi.com/api/episode/35\",\"https://rickandmortyapi.com/api/episode/36\",\"https://rickandmortyapi.com/api/episode/38\",\"https://rickandmortyapi.com/api/episode/39\",\"https://rickandmortyapi.com/api/episode/40\",\"https://rickandmortyapi.com/api/episode/41\",\"https://rickandmortyapi.com/api/episode/42\",\"https://rickandmortyapi.com/api/episode/43\",\"https://rickandmortyapi.com/api/episode/44\",\"https://rickandmortyapi.com/api/episode/45\",\"https://rickandmortyapi.com/api/episode/46\",\"https://rickandmortyapi.com/api/episode/47\",\"https://rickandmortyapi.com/api/episode/48\",\"https://rickandmortyapi.com/api/episode/49\",\"https://rickandmortyapi.com/api/episode/51\"],\"url\":\"https://rickandmortyapi.com/api/character/3\",\"created\":\"2017-11-04T19:09:56.428Z\"},{\"id\":4,\"name\":\"Beth Smith\",\"status\":\"Alive\",\"species\":\"Human\",\"type\":\"\",\"gender\":\"Female\",\"origin\":{\"name\":\"Earth (Replacement Dimension)\",\"url\":\"https://rickandmortyapi.com/api/location/20\"},\"location\":{\"name\":\"Earth (Replacement Dimension)\",\"url\":\"https://rickandmortyapi.com/api/location/20\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/4.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/6\",\"https://rickandmortyapi.com/api/episode/7\",\"https://rickandmortyapi.com/api/episode/8\",\"https://rickandmortyapi.com/api/episode/9\",\"https://rickandmortyapi.com/api/episode/10\",\"https://rickandmortyapi.com/api/episode/11\",\"https://rickandmortyapi.com/api/episode/12\",\"https://rickandmortyapi.com/api/episode/14\",\"https://rickandmortyapi.com/api/episode/15\",\"https://rickandmortyapi.com/api/episode/16\",\"https://rickandmortyapi.com/api/episode/18\",\"https://rickandmortyapi.com/api/episode/19\",\"https://rickandmortyapi.com/api/episode/20\",\"https://rickandmortyapi.com/api/episode/21\",\"https://rickandmortyapi.com/api/episode/22\",\"https://rickandmortyapi.com/api/episode/23\",\"https://rickandmortyapi.com/api/episode/24\",\"https://rickandmortyapi.com/api/episode/25\",\"https://rickandmortyapi.com/api/episode/26\",\"https://rickandmortyapi.com/api/episode/27\",\"https://rickandmortyapi.com/api/episode/28\",\"https://rickandmortyapi.com/api/episode/29\",\"https://rickandmortyapi.com/api/episode/30\",\"https://rickandmortyapi.com/api/episode/31\",\"https://rickandmortyapi.com/api/episode/32\",\"https://rickandmortyapi.com/api/episode/33\",\"https://rickandmortyapi.com/api/episode/34\",\"https://rickandmortyapi.com/api/episode/35\",\"https://rickandmortyapi.com/api/episode/36\",\"https://rickandmortyapi.com/api/episode/38\",\"https://rickandmortyapi.com/api/episode/39\",\"https://rickandmortyapi.com/api/episode/40\",\"https://rickandmortyapi.com/api/episode/41\",\"https://rickandmortyapi.com/api/episode/42\",\"https://rickandmortyapi.com/api/episode/43\",\"https://rickandmortyapi.com/api/episode/44\",\"https://rickandmortyapi.com/api/episode/45\",\"https://rickandmortyapi.com/api/episode/46\",\"https://rickandmortyapi.com/api/episode/47\",\"https://rickandmortyapi.com/api/episode/48\",\"https://rickandmortyapi.com/api/episode/49\",\"https://rickandmortyapi.com/api/episode/51\"],\"url\":\"https://rickandmortyapi.com/api/character/4\",\"created\":\"2017-11-04T19:22:43.665Z\"},{\"id\":5,\"name\":\"Jerry Smith\",\"status\":\"Alive\",\"species\":\"Human\",\"type\":\"\",\"gender\":\"Male\",\"origin\":{\"name\":\"Earth (Replacement Dimension)\",\"url\":\"https://rickandmortyapi.com/api/location/20\"},\"location\":{\"name\":\"Earth (Replacement Dimension)\",\"url\":\"https://rickandmortyapi.com/api/location/20\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/5.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/6\",\"https://rickandmortyapi.com/api/episode/7\",\"https://rickandmortyapi.com/api/episode/8\",\"https://rickandmortyapi.com/api/episode/9\",\"https://rickandmortyapi.com/api/episode/10\",\"https://rickandmortyapi.com/api/episode/11\",\"https://rickandmortyapi.com/api/episode/12\",\"https://rickandmortyapi.com/api/episode/13\",\"https://rickandmortyapi.com/api/episode/14\",\"https://rickandmortyapi.com/api/episode/15\",\"https://rickandmortyapi.com/api/episode/16\",\"https://rickandmortyapi.com/api/episode/18\",\"https://rickandmortyapi.com/api/episode/19\",\"https://rickandmortyapi.com/api/episode/20\",\"https://rickandmortyapi.com/api/episode/21\",\"https://rickandmortyapi.com/api/episode/22\",\"https://rickandmortyapi.com/api/episode/23\",\"https://rickandmortyapi.com/api/episode/26\",\"https://rickandmortyapi.com/api/episode/29\",\"https://rickandmortyapi.com/api/episode/30\",\"https://rickandmortyapi.com/api/episode/31\",\"https://rickandmortyapi.com/api/episode/32\",\"https://rickandmortyapi.com/api/episode/33\",\"https://rickandmortyapi.com/api/episode/35\",\"https://rickandmortyapi.com/api/episode/36\",\"https://rickandmortyapi.com/api/episode/38\",\"https://rickandmortyapi.com/api/episode/39\",\"https://rickandmortyapi.com/api/episode/40\",\"https://rickandmortyapi.com/api/episode/41\",\"https://rickandmortyapi.com/api/episode/42\",\"https://rickandmortyapi.com/api/episode/43\",\"https://rickandmortyapi.com/api/episode/44\",\"https://rickandmortyapi.com/api/episode/45\",\"https://rickandmortyapi.com/api/episode/46\",\"https://rickandmortyapi.com/api/episode/47\",\"https://rickandmortyapi.com/api/episode/48\",\"https://rickandmortyapi.com/api/episode/49\",\"https://rickandmortyapi.com/api/episode/50\",\"https://rickandmortyapi.com/api/episode/51\"],\"url\":\"https://rickandmortyapi.com/api/character/5\",\"created\":\"2017-11-04T19:26:56.301Z\"},{\"id\":6,\"name\":\"Abadango Cluster Princess\",\"status\":\"Alive\",\"species\":\"Alien\",\"type\":\"\",\"gender\":\"Female\",\"origin\":{\"name\":\"Abadango\",\"url\":\"https://rickandmortyapi.com/api/location/2\"},\"location\":{\"name\":\"Abadango\",\"url\":\"https://rickandmortyapi.com/api/location/2\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/6.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/27\"],\"url\":\"https://rickandmortyapi.com/api/character/6\",\"created\":\"2017-11-04T19:50:28.250Z\"},{\"id\":7,\"name\":\"Abradolf Lincler\",\"status\":\"unknown\",\"species\":\"Human\",\"type\":\"Genetic experiment\",\"gender\":\"Male\",\"origin\":{\"name\":\"Earth (Replacement Dimension)\",\"url\":\"https://rickandmortyapi.com/api/location/20\"},\"location\":{\"name\":\"Testicle Monster Dimension\",\"url\":\"https://rickandmortyapi.com/api/location/21\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/7.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/10\",\"https://rickandmortyapi.com/api/episode/11\"],\"url\":\"https://rickandmortyapi.com/api/character/7\",\"created\":\"2017-11-04T19:59:20.523Z\"},{\"id\":8,\"name\":\"Adjudicator Rick\",\"status\":\"Dead\",\"species\":\"Human\",\"type\":\"\",\"gender\":\"Male\",\"origin\":{\"name\":\"unknown\",\"url\":\"\"},\"location\":{\"name\":\"Citadel of Ricks\",\"url\":\"https://rickandmortyapi.com/api/location/3\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/8.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/28\"],\"url\":\"https://rickandmortyapi.com/api/character/8\",\"created\":\"2017-11-04T20:03:34.737Z\"},{\"id\":9,\"name\":\"Agency Director\",\"status\":\"Dead\",\"species\":\"Human\",\"type\":\"\",\"gender\":\"Male\",\"origin\":{\"name\":\"Earth (Replacement Dimension)\",\"url\":\"https://rickandmortyapi.com/api/location/20\"},\"location\":{\"name\":\"Earth (Replacement Dimension)\",\"url\":\"https://rickandmortyapi.com/api/location/20\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/9.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/24\"],\"url\":\"https://rickandmortyapi.com/api/character/9\",\"created\":\"2017-11-04T20:06:54.976Z\"},{\"id\":10,\"name\":\"Alan Rails\",\"status\":\"Dead\",\"species\":\"Human\",\"type\":\"Superhuman (Ghost trains summoner)\",\"gender\":\"Male\",\"origin\":{\"name\":\"unknown\",\"url\":\"\"},\"location\":{\"name\":\"Worldender's lair\",\"url\":\"https://rickandmortyapi.com/api/location/4\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/10.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/25\"],\"url\":\"https://rickandmortyapi.com/api/character/10\",\"created\":\"2017-11-04T20:19:09.017Z\"},{\"id\":11,\"name\":\"Albert Einstein\",\"status\":\"Dead\",\"species\":\"Human\",\"type\":\"\",\"gender\":\"Male\",\"origin\":{\"name\":\"Earth (C-137)\",\"url\":\"https://rickandmortyapi.com/api/location/1\"},\"location\":{\"name\":\"Earth (Replacement Dimension)\",\"url\":\"https://rickandmortyapi.com/api/location/20\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/11.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/12\"],\"url\":\"https://rickandmortyapi.com/api/character/11\",\"created\":\"2017-11-04T20:20:20.965Z\"},{\"id\":12,\"name\":\"Alexander\",\"status\":\"Dead\",\"species\":\"Human\",\"type\":\"\",\"gender\":\"Male\",\"origin\":{\"name\":\"Earth (C-137)\",\"url\":\"https://rickandmortyapi.com/api/location/1\"},\"location\":{\"name\":\"Anatomy Park\",\"url\":\"https://rickandmortyapi.com/api/location/5\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/12.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/3\"],\"url\":\"https://rickandmortyapi.com/api/character/12\",\"created\":\"2017-11-04T20:32:33.144Z\"},{\"id\":13,\"name\":\"Alien Googah\",\"status\":\"unknown\",\"species\":\"Alien\",\"type\":\"\",\"gender\":\"unknown\",\"origin\":{\"name\":\"unknown\",\"url\":\"\"},\"location\":{\"name\":\"Earth (Replacement Dimension)\",\"url\":\"https://rickandmortyapi.com/api/location/20\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/13.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/31\"],\"url\":\"https://rickandmortyapi.com/api/character/13\",\"created\":\"2017-11-04T20:33:30.779Z\"},{\"id\":14,\"name\":\"Alien Morty\",\"status\":\"unknown\",\"species\":\"Alien\",\"type\":\"\",\"gender\":\"Male\",\"origin\":{\"name\":\"unknown\",\"url\":\"\"},\"location\":{\"name\":\"Citadel of Ricks\",\"url\":\"https://rickandmortyapi.com/api/location/3\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/14.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/10\"],\"url\":\"https://rickandmortyapi.com/api/character/14\",\"created\":\"2017-11-04T20:51:31.373Z\"},{\"id\":15,\"name\":\"Alien Rick\",\"status\":\"unknown\",\"species\":\"Alien\",\"type\":\"\",\"gender\":\"Male\",\"origin\":{\"name\":\"unknown\",\"url\":\"\"},\"location\":{\"name\":\"Citadel of Ricks\",\"url\":\"https://rickandmortyapi.com/api/location/3\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/15.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/10\"],\"url\":\"https://rickandmortyapi.com/api/character/15\",\"created\":\"2017-11-04T20:56:13.215Z\"},{\"id\":16,\"name\":\"Amish Cyborg\",\"status\":\"Dead\",\"species\":\"Alien\",\"type\":\"Parasite\",\"gender\":\"Male\",\"origin\":{\"name\":\"unknown\",\"url\":\"\"},\"location\":{\"name\":\"Earth (Replacement Dimension)\",\"url\":\"https://rickandmortyapi.com/api/location/20\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/16.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/15\"],\"url\":\"https://rickandmortyapi.com/api/character/16\",\"created\":\"2017-11-04T21:12:45.235Z\"},{\"id\":17,\"name\":\"Annie\",\"status\":\"Alive\",\"species\":\"Human\",\"type\":\"\",\"gender\":\"Female\",\"origin\":{\"name\":\"Earth (C-137)\",\"url\":\"https://rickandmortyapi.com/api/location/1\"},\"location\":{\"name\":\"Anatomy Park\",\"url\":\"https://rickandmortyapi.com/api/location/5\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/17.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/3\"],\"url\":\"https://rickandmortyapi.com/api/character/17\",\"created\":\"2017-11-04T22:21:24.481Z\"},{\"id\":18,\"name\":\"Antenna Morty\",\"status\":\"Alive\",\"species\":\"Human\",\"type\":\"Human with antennae\",\"gender\":\"Male\",\"origin\":{\"name\":\"unknown\",\"url\":\"\"},\"location\":{\"name\":\"Citadel of Ricks\",\"url\":\"https://rickandmortyapi.com/api/location/3\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/18.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/10\",\"https://rickandmortyapi.com/api/episode/28\"],\"url\":\"https://rickandmortyapi.com/api/character/18\",\"created\":\"2017-11-04T22:25:29.008Z\"},{\"id\":19,\"name\":\"Antenna Rick\",\"status\":\"unknown\",\"species\":\"Human\",\"type\":\"Human with antennae\",\"gender\":\"Male\",\"origin\":{\"name\":\"unknown\",\"url\":\"\"},\"location\":{\"name\":\"unknown\",\"url\":\"\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/19.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/10\"],\"url\":\"https://rickandmortyapi.com/api/character/19\",\"created\":\"2017-11-04T22:28:13.756Z\"},{\"id\":20,\"name\":\"Ants in my Eyes Johnson\",\"status\":\"unknown\",\"species\":\"Human\",\"type\":\"Human with ants in his eyes\",\"gender\":\"Male\",\"origin\":{\"name\":\"unknown\",\"url\":\"\"},\"location\":{\"name\":\"Interdimensional Cable\",\"url\":\"https://rickandmortyapi.com/api/location/6\"},\"image\":\"https://rickandmortyapi.com/api/character/avatar/20.jpeg\",\"episode\":[\"https://rickandmortyapi.com/api/episode/8\"],\"url\":\"https://rickandmortyapi.com/api/character/20\",\"created\":\"2017-11-04T22:34:53.659Z\"}]}"
        ).results.toString())

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
                        Log.e("gaypop", "invoked search")
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