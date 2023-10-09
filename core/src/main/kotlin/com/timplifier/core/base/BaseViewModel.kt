package com.timplifier.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.paging.rxjava2.cachedIn
import com.timplifier.common.either.Either
import com.timplifier.core.ui.state.UIState
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected val disposable = CompositeDisposable()

    protected fun <T> mutableUiStateFlow() = MutableStateFlow<UIState<T>>(UIState.Idle())

    protected fun <T> uiObservable() = PublishSubject.create<UIState<T>> { emitter ->
        emitter.onNext(UIState.Idle())
    }

    protected fun <T> uiBehaviorSubject() =
        BehaviorSubject.createDefault<UIState<T>>(UIState.Idle())

    protected fun <T, S> Flow<Either<String, T>>.gatherRequest(
        state: MutableStateFlow<UIState<S>>,
        mappedData: (data: T) -> S,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            state.value = UIState.Loading()
            this@gatherRequest.collect {
                when (it) {
                    is Either.Left -> state.value = UIState.Error(it.value)
                    is Either.Right -> state.value =
                        UIState.Success(mappedData(it.value))
                }
            }
        }
    }

    protected fun <T, S> Observable<Either<String, T>>.gatherRequest(
        observable: BehaviorSubject<UIState<S>>,
        mappedData: (data: T) -> S,
    ) {
        disposable.add(subscribeOn(Schedulers.io()).subscribe { result ->
            observable.onNext(UIState.Loading())
            when (result) {
                is Either.Left -> observable.onNext(UIState.Error(result.value))
                is Either.Right -> observable.onNext(
                    UIState.Success(mappedData(result.value))
                )
            }
        })
    }

    protected fun <T> Flow<Either<String, T>>.gatherRequest(
        state: MutableStateFlow<UIState<T>>,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            state.value = UIState.Loading()
            this@gatherRequest.collect {
                when (it) {
                    is Either.Left -> state.value = UIState.Error(it.value)
                    is Either.Right -> state.value =
                        UIState.Success(it.value)
                }
            }
        }
    }

    protected fun <T : Any, S : Any> Flow<PagingData<T>>.gatherPagingRequest(
        mappedData: (data: T) -> S,
    ) = map {
        it.map { data -> mappedData(data) }
    }.cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun <T : Any, S : Any> Flowable<PagingData<T>>.gatherPagingRequest(
        mappedData: (data: T) -> S,
    ) = map { it.map { data -> mappedData(data) } }.cachedIn(viewModelScope)

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}