package com.timplifier.main.presentation.ui.fragments.main.characters

import com.timplifier.core.base.BaseViewModel
import com.timplifier.domain.useCases.FetchCharactersUseCase
import com.timplifier.domain.useCases.FetchSingleEpisodeUseCase
import com.timplifier.domain.useCases.GetCharactersUseCase
import com.timplifier.domain.useCases.GetSingleEpisodeUseCase
import com.timplifier.main.presentation.models.CharacterUI
import com.timplifier.main.presentation.models.toUI
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class CharactersViewModel @Inject constructor(
    private val fetchCharactersUseCase: FetchCharactersUseCase,
    private val fetchSingleEpisodeUseCase: FetchSingleEpisodeUseCase,
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getSingleEpisodeUseCase: GetSingleEpisodeUseCase
) :
    BaseViewModel() {

    private val _localCharactersState =
        BehaviorSubject.createDefault<List<CharacterUI>>(emptyList())
    val localCharactersState: Observable<List<CharacterUI>> = _localCharactersState

    private val _searchQueryState = BehaviorSubject.createDefault("")
    val searchQueryState: Observable<String?> = _searchQueryState

    fun fetchCharacters(
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ) =
        fetchCharactersUseCase(
            _searchQueryState.value,
            status,
            species,
            gender
        ).gatherPagingRequest {
            it.toUI()
        }

    fun fetchSingleEpisode(id: Int) =
        fetchSingleEpisodeUseCase(id)

    fun getLocalCharacters(
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ) {
        Observable.defer {
            getCharactersUseCase(_searchQueryState.value, status, species, gender)
                .map { it.map { characterModel -> characterModel.toUI() } }
        }
            .subscribeOn(Schedulers.io())
            .subscribe { localCharacters ->
                _localCharactersState.onNext(localCharacters)
            }
    }

    fun getSingleEpisode(url: String) = getSingleEpisodeUseCase(url)

    fun modifySearchQuery(newQuery: String) {
        _searchQueryState.onNext(newQuery)
    }
}