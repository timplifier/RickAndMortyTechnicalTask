package com.timplifier.main.presentation.ui.fragments.main.characters.detail

import com.timplifier.core.base.BaseViewModel
import com.timplifier.core.ui.state.UIState
import com.timplifier.domain.useCases.FetchSingleCharacterUseCase
import com.timplifier.domain.useCases.GetSingleCharacterUseCase
import com.timplifier.main.presentation.models.CharacterUI
import com.timplifier.main.presentation.models.toUI
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class CharacterDetailViewModel @Inject constructor(
    private val fetchSingleCharacterUseCase: FetchSingleCharacterUseCase,
    private val getSingleCharacterUseCase: GetSingleCharacterUseCase
) : BaseViewModel() {

    private val _characterState = uiBehaviorSubject<CharacterUI>()
    val characterState: Observable<UIState<CharacterUI>> = _characterState

    private val _localCharacterState =
        BehaviorSubject.create<CharacterUI?>()
    val localCharacterState: Observable<CharacterUI> = _localCharacterState

    fun fetchSingleCharacter(id: Int) {
        fetchSingleCharacterUseCase(id).gatherRequest(_characterState) { it.toUI() }
    }

    fun getSingleCharacter(id: Int) {
        disposable.add(Observable.defer {
            getSingleCharacterUseCase(id).map { characterModel -> characterModel.toUI() }
        }
            .subscribeOn(Schedulers.io())
            .subscribe { localCharacter ->
                _localCharacterState.onNext(localCharacter)
            })
    }
}